package utility;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javaConstants.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class CronJob implements Job {

	private static SchedulerFactory schedFact = new StdSchedulerFactory();

	private static Scheduler sched;

	static {
		try {
			sched = schedFact.getScheduler();
		} catch (SchedulerException e) {
			System.out.println("Exception , Exiting the program " + e);
			System.exit(-1);
		}
	}

	public static void ScheduleCronJob(HttpServletRequest req,
			HttpServletResponse res) throws Exception {

		try {

			sched.deleteJob("MailSend", "Job1");

			JobDetail jobDetail = new JobDetail("MailSend", "Job1",
					CronJob.class);

			ReadPropertiesFile.readConfig(req);

			CronTrigger trigger = new CronTrigger("sendMailJob",
					"triggerGroup1");

			SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
			Date date = parseFormat.parse(Constants.timeToRun);

			SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
			String date1 = displayFormat.format(date);

			String[] timeSplit = date1.split(":");

			trigger.setCronExpression("0 " + timeSplit[1] + " " + timeSplit[0]
					+ " * * ?");

			sched.getContext().put("request", req.getRequestURL().toString());

			sched.start();
			sched.scheduleJob(jobDetail, trigger);

		} catch (SchedulerException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	public void execute(JobExecutionContext cntxt) throws JobExecutionException {

		try {
			SchedulerContext schedulerContext = cntxt.getScheduler()
					.getContext();

			String request1 = (String) schedulerContext.get("request");

			sendMail(request1);
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void unscheduleCronJob(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		sched.deleteJob("MailSend", "Job1");
		GenericUtility.callGetOfController(req, res);
	}

	private void sendMail(String req) throws IOException {

		GenericUtility.callGetForMail(req);

	}

}