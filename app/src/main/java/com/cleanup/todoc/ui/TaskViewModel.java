package com.cleanup.todoc.ui;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends AndroidViewModel{

    // REPOSITORIES
    private final TaskDataRepository taskDataSource;
    private final ProjectDataRepository projectDataSource;
    private final Executor executor;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Project>> allProjects;

    // DATA
    @Nullable
    private LiveData<Project> currentProject;

    public TaskViewModel(Application application, TaskDataRepository taskDataSource, ProjectDataRepository projectDataSource, Executor executor) {
        super(application);
        this.taskDataSource = taskDataSource;
        this.projectDataSource = projectDataSource;
        this.executor = executor;
        allTasks = taskDataSource.getAllTasks();
        allProjects = projectDataSource.getAllProjects();

    }

    public void init(long projectId) {
        if (this.currentProject != null) {
            return;
        }
        currentProject = projectDataSource.getProject(projectId);
    }

    // -------------
    // FOR PROJECT
    // -------------

    public LiveData<Project> getProject(long projectId) { return this.currentProject;  }
    public LiveData<List<Project>> getAllProjects() { return allProjects;  }



    // -------------
    // FOR TASK
    // -------------

    public LiveData<List<Task>> getTasks(long projectId) {
        return taskDataSource.getTasks(projectId);
    }

    public void createTask(Task task) {
        executor.execute(() -> taskDataSource.createTask(task));
    }

    public void deleteTask(long taskId) {
        executor.execute(() -> taskDataSource.deleteTask(taskId));
    }

    public void updateTask(Task task) {
        executor.execute(() -> taskDataSource.updateTask(task));
    }

    public LiveData<List<Task>> getAllTasks(){
        return allTasks;
    }
}
