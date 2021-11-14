package com.cleanup.todoc.database;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

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
                            .addCallback(roomCallBack)
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

                ContentValues projectValues = new ContentValues();
                projectValues.put("id", 1L);
                projectValues.put("name", "Projet Futur");
                projectValues.put("color", "0xFFEADAD1");

                projectValues.put("id", 2L);
                projectValues.put("name", "Projet Hercule");
                projectValues.put("color", "0xFFB4CDBA");

                projectValues.put("id", 3L);
                projectValues.put("name", "Projet Thor");
                projectValues.put("color", "0xFFA3CED2");

                ContentValues taskValues = new ContentValues();
                taskValues.put("id", 1);
                taskValues.put("projectId", 1L);
                taskValues.put("name", "Nettoyer les vitres");
                taskValues.put("creationTimestamp", 1636921060000L);


                db.insert("Project", OnConflictStrategy.IGNORE, projectValues);
                db.insert("Task", OnConflictStrategy.IGNORE, taskValues);
            }
        };
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){

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
    }

}
