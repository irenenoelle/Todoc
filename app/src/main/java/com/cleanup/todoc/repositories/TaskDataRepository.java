package com.cleanup.todoc.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class TaskDataRepository {
    private final TaskDao taskDao;

    public TaskDataRepository(TaskDao taskDao) { this.taskDao = taskDao; }

    // --- GET ---

    public LiveData<List<Task>> getTasks(long projectId){ return this.taskDao.getTasks(projectId); }

    // --- CREATE ---

    public void createTask(Task task){ taskDao.insertTask(task); }

    // --- DELETE ---
    public void deleteTask(long taskId){ taskDao.deleteTask(taskId); }

    // --- UPDATE ---
    public void updateTask(Task task){ taskDao.updateTask(task); }

    public LiveData<List<Task>> getAllTasks() { return taskDao.getAllTasks(); }

}
