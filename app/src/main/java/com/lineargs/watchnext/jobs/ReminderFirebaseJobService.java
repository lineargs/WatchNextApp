package com.lineargs.watchnext.jobs;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;

/**
 * Created by goranminov on 03/12/2017.
 * <p>
 * See {@link JobService}
 */

public class ReminderFirebaseJobService extends JobService {

    /**
     * The entry point to our Job. Implementations should offload work to another thread of
     * execution as soon as possible.
     * <p>
     * This is called by the Job Dispatcher to tell us we should start our job. Keep in mind this
     * method is run on the application's main thread, so we need to offload work to a background
     * thread.
     *
     * @return whether there is more work remaining.
     */
    @Override
    public boolean onStartJob(final JobParameters job) {
        Bundle bundle = job.getExtras();
        final int id = bundle.getInt(ReminderFirebaseUtilities.ID);
        final String title = bundle.getString(ReminderFirebaseUtilities.TITLE);
        final String name = bundle.getString(ReminderFirebaseUtilities.NAME);
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = ReminderFirebaseJobService.this;
                ReminderFirebaseTasks.executeTask(context, id, title, name, ReminderFirebaseTasks.ACTION_REMINDER);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                /*
                 * Once the AsyncTask is finished, the job is finished. To inform JobManager that
                 * we're done, we call jobFinished with the jobParamters that were passed to our
                 * job and a boolean representing whether the job needs to be rescheduled. This is
                 * usually if something didn't work and we want the job to try running again.
                 */
                jobFinished(job, false);
            }
        };
        asyncTask.execute();
        return false;
    }

    /**
     * Called when the scheduling engine has decided to interrupt the execution of a running job,
     * most likely because the runtime constraints associated with the job are no longer satisfied.
     *
     * @return whether the job should be retried
     * @see Job.Builder#setRetryStrategy(RetryStrategy)
     * @see RetryStrategy
     */
    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
