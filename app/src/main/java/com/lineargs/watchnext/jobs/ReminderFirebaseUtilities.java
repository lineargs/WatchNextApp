package com.lineargs.watchnext.jobs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by goranminov on 03/12/2017.
 * <p>
 * Helper class used to schedule our reminder
 * See {@link FirebaseJobDispatcher}
 */

public class ReminderFirebaseUtilities {

    static final String ID = "id", TITLE = "title", NAME = "name";
    private static final int SYNC_FLEXTIME_MINUTES = 60;
    private static final int SYNC_FLEXTIME_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(SYNC_FLEXTIME_MINUTES));

    synchronized public static void scheduleReminder(@NonNull final Context context, int reminderSeconds, int id, String title, String name) {
        /*
         * Since we pass information about our notification reminder
         * we put them in bundle so we can pass them as extras.
         */
        Bundle bundle = new Bundle();
        bundle.putInt(ID, id);
        bundle.putString(TITLE, title);
        bundle.putString(NAME, name);

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        /* Create the Job to show reminder notification in certain time frame*/
        Job constraintReminderJob = dispatcher.newJobBuilder()
                /* The Service that will be used to write to preferences */
                .setService(ReminderFirebaseJobService.class)
                /*
                 * Set the UNIQUE tag used to identify this Job.
                 * In our case we pass the Episode ID
                 */
                .setTag(String.valueOf(id))
                /* Network constraints on which this Job should run. */
                .setConstraints(Constraint.ON_ANY_NETWORK)
                /*
                 * setLifetime sets how long this job should persist. The options are to keep the
                 * Job "forever" or to have it die the next time the device boots up.
                 */
                .setLifetime(Lifetime.FOREVER)
                /*
                 * We don't want these reminders to continuously happen, so we tell this Job not to recur.
                 */
                .setRecurring(false)
                /*
                 * The first argument for Trigger class's static executionWindow method
                 * is the start of the time frame when the
                 * job should be performed. The second argument is the latest point in time at
                 * which the data should be synced. Please note that this end time is not
                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
                 */
                .setTrigger(Trigger.executionWindow(
                        reminderSeconds,
                        reminderSeconds + SYNC_FLEXTIME_SECONDS))
                /*
                 * If a Job with the tag provided already exists, this new job will replace
                 * the old one.
                 */
                .setReplaceCurrent(true)
                /* In our case we pass id, title and name*/
                .setExtras(bundle)
                /* Once the Job is ready, call the builder's build method to return the Job */
                .build();

        /* Schedule the Job with the dispatcher */
        dispatcher.schedule(constraintReminderJob);
    }
}
