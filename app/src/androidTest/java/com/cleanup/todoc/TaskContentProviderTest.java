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
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.database.SaveTasksDatabase;
import com.cleanup.todoc.provider.TaskContentProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TaskContentProviderTest {

    // FOR DATA
    private ContentResolver mContentResolver;

    // DATA SET FOR TEST
    private static long PROJECT_ID = 1L;

    @Before
    public void setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                SaveTasksDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getContext().getContentResolver();
    }

    @Test
    public void getTasksWhenNoTaskInserted() {
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(TaskContentProvider.URI_ITEM, PROJECT_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void insertAndGetTask() {
        // BEFORE : Adding demo task
        final Uri userUri = mContentResolver.insert(TaskContentProvider.URI_ITEM, generateItem());
        // TEST
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(TaskContentProvider.URI_ITEM, PROJECT_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("name")), is("Vider les poubelles"));
    }

    // ---

    private ContentValues generateItem(){
        final ContentValues values = new ContentValues();
        values.put("id", 4);
        values.put("projectId", 1L);
        values.put("name", "Vider les poubelles");
        values.put("creationTimeStamp", 1636921060000L);
        return values;
    }
}
