package com.cleanup.todoc.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.Date;

@Database(entities = {Task.class, Project.class}, version = 1, exportSchema = false)
public abstract class SaveTasksDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile SaveTasksDatabase INSTANCE;

    // --- DAO ---
    public abstract TaskDao taskDao();
    public abstract ProjectDao projectDao();

    // --- INSTANCE ---
    public static synchronized SaveTasksDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SaveTasksDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SaveTasksDatabase.class, "task_database.db").fallbackToDestructiveMigration()
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase(){
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues projectValues1 = new ContentValues();
                projectValues1.put("id", 1L);
                projectValues1.put("name", "Projet Futur");
                projectValues1.put("color", "0xFFEADAD1");

                ContentValues projectValues2 = new ContentValues();
                projectValues2.put("id", 2L);
                projectValues2.put("name", "Projet Hercule");
                projectValues2.put("color", "0xFFB4CDBA");

                ContentValues projectValues3 = new ContentValues();
                projectValues3.put("id", 3L);
                projectValues3.put("name", "Projet Thor");
                projectValues3.put("color", "0xFFA3CED2");

                ContentValues taskValues1 = new ContentValues();
                taskValues1.put("id", 1);
                taskValues1.put("projectId", 1L);
                taskValues1.put("name", "Nettoyer les vitres");
                taskValues1.put("creationTimestamp", 1636921060000L);

                ContentValues taskValues2 = new ContentValues();
                taskValues2.put("id", 2);
                taskValues2.put("projectId", 2L);
                taskValues2.put("name", "Passer l'aspirateur");
                taskValues2.put("creationTimestamp", new Date().getTime());

                ContentValues taskValues3 = new ContentValues();
                taskValues3.put("id", 3);
                taskValues3.put("projectId", 3L);
                taskValues3.put("name", "Nettoyer les toilettes");
                taskValues3.put("creationTimestamp", new Date().getTime());

                // Insert all projects
                db.insert("Project", OnConflictStrategy.IGNORE, projectValues1);
                db.insert("Project", OnConflictStrategy.IGNORE, projectValues2);
                db.insert("Project", OnConflictStrategy.IGNORE, projectValues3);

                db.insert("Task", OnConflictStrategy.IGNORE, taskValues1);
                db.insert("Task", OnConflictStrategy.IGNORE, taskValues2);
                db.insert("Task", OnConflictStrategy.IGNORE, taskValues3);

            }
        };
    }

    /* private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
                new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private TaskDao taskDao;
        private ProjectDao projectDao;

        public PopulateDbAsyncTask(SaveTasksDatabase db) {
            this.taskDao = db.taskDao();
            this.projectDao = db.projectDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.insertTask(new Task(1,1L,"Nettoyer les vitres", new Date().getTime()));
            taskDao.insertTask(new Task(2,2L,"Passer l'aspirateur", new Date().getTime()));
            taskDao.insertTask(new Task(3,3L,"Nettoyer les toilettes", new Date().getTime()));
            return null;
       }
    }*/

}
