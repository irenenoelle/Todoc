package com.cleanup.todoc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;


import com.cleanup.todoc.database.SaveTasksDatabase;
import com.cleanup.todoc.provider.TaskContentProvider;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TaskContentProviderTest {

    // FOR DATA
    private ContentResolver mContentResolver;

    // DATA SET FOR TEST
    private static long PROJECT_ID = 1;

    @Before
    public void setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                SaveTasksDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getContext().getContentResolver();
    }

    @Test
    public void getTasksBeforeInsertTask() {
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(TaskContentProvider.URI_ITEM, PROJECT_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void insertAndGetTask() {
        // BEFORE : Adding demo task
        final Uri userUri = mContentResolver.insert(TaskContentProvider.URI_ITEM, generateTask());
        // TEST
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(TaskContentProvider.URI_ITEM, PROJECT_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToLast(), is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("name")), is("Vider les poubelles"));
    }

    // ---

    private ContentValues generateTask(){
        final ContentValues values = new ContentValues();
        values.put("id", 4);
        values.put("projectId", 1L);
        values.put("name", "Vider les poubelles");
        values.put("creationTimeStamp", 1636921060000L);
        return values;
    }
}
