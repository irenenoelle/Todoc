package com.cleanup.todoc.model;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.cleanup.todoc.ui.MainActivity;
import com.cleanup.todoc.ui.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Models for project in which tasks are included.</p>
 *
 * @author Gaëtan HERFRAY
 */

@Entity
public class Project {
    private static TaskViewModel taskViewModel;
    private static List<Project> allProjects = new ArrayList<Project>();

    /**
     * The unique identifier of the project
     */
    @PrimaryKey
    private long id;

    /**
     * The name of the project
     */
    @NonNull
    private final String name;

    /**
     * The hex (ARGB) code of the color associated to the project
     */
    @ColorInt
    private final int color;

    /**
     * Instantiates a new Project.
     *
     * @param id    the unique identifier of the project to set
     * @param name  the name of the project to set
     * @param color the hex (ARGB) code of the color associated to the project to set
     */
    public Project(long id, @NonNull String name, @ColorInt int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    /**
     * Returns all the projects of the application.
     *
     * @return all the projects of the application
     */
    @NonNull
    public static Project[] getAllProjects() {
        return null;
        /*return new Project[]{
                new Project(1L, "Projet Tartampion", 0xFFEADAD1),
                new Project(2L, "Projet Lucidia", 0xFFB4CDBA),
                new Project(3L, "Projet Circus", 0xFFA3CED2),
        };*/
    }

    /**
     * Returns the project with the given unique identifier, or null if no project with that
     * identifier can be found.
     *
     * @param id the unique identifier of the project to return
     * @return the project with the given unique identifier, or null if it has not been found
     */
    @Nullable
    public static Project getProjectById(long id) {
        taskViewModel.getAllProjects().observe(getApplicationContext(),
                new Observer<List<Project>>() {
                    @Override
                    public void onChanged(List<Project> projects) {
                        allProjects = projects;
                    }
                });
        for (Project project : allProjects) {
            if (project.id == id)
                return project;
        }
        return null;
    }

    /**
     * Returns the unique identifier of the project.
     *
     * @return the unique identifier of the project
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the name of the project.
     *
     * @return the name of the project
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Returns the hex (ARGB) code of the color associated to the project.
     *
     * @return the hex (ARGB) code of the color associated to the project
     */
    @ColorInt
    public int getColor() {
        return color;
    }

    @Override
    @NonNull
    public String toString() {
        return getName();
    }

    @Ignore
    public Project(@NonNull String name, int color) {
        this.name = name;
        this.color = color;
    }
    public void setId(long id) { this.id = id; }

}
