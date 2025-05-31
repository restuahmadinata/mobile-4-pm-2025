package com.example.noted;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.OnBackPressedCallback;
import com.example.noted.db.NoteDatabaseHelper;
import com.example.noted.model.Note;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EditNoteActivity extends AppCompatActivity {

    private TextInputEditText editTitle, editContent;
    private NoteDatabaseHelper dbHelper;
    private long noteId;
    private Note currentNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.edit), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupBackPressedCallback();


        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        editTitle = findViewById(R.id.edit_title);
        editContent = findViewById(R.id.edit_content);
        MaterialButton updateButton = findViewById(R.id.button_update);
        MaterialButton deleteButton = findViewById(R.id.button_delete);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new NoteDatabaseHelper(this);

        noteId = getIntent().getLongExtra("note_id", -1);
        if (noteId == -1) {
            Toast.makeText(this, "Error: Note not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadNoteData();
        updateButton.setOnClickListener(v -> updateNote());
        deleteButton.setOnClickListener(v -> confirmDeleteNote());
    }

    private void loadNoteData() {
        currentNote = dbHelper.getNote(noteId);
        if (currentNote != null) {
            editTitle.setText(currentNote.getTitle());
            editContent.setText(currentNote.getContent());
        } else {
            Toast.makeText(this, "Error: Note not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateNote() {
        String title = editTitle.getText().toString().trim();
        String content = editContent.getText().toString().trim();

        // Simple validation
        if (title.isEmpty()) {
            editTitle.setError("Title cannot be empty");
            return;
        }

        // Update the note data
        currentNote.setTitle(title);
        currentNote.setContent(content);
        currentNote.setUpdatedAt(System.currentTimeMillis());

        // Update in database
        int rowsAffected = dbHelper.updateNote(currentNote);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error updating note", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDeleteNote() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete", (dialog, which) -> deleteNote())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteNote() {
        dbHelper.deleteNote(noteId);
        Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (hasChanges()) {
                showDiscardChangesDialog();
            } else {
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean hasChanges() {
        String currentTitle = editTitle.getText().toString().trim();
        String currentContent = editContent.getText().toString().trim();

        return !currentTitle.equals(currentNote.getTitle()) ||
                !currentContent.equals(currentNote.getContent());
    }

    private void showDiscardChangesDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Discard Changes")
                .setMessage("You have unsaved changes. Do you want to discard them?")
                .setPositiveButton("Discard", (dialog, which) -> finish())
                .setNegativeButton("Continue Editing", null)
                .show();
    }

    private void setupBackPressedCallback() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (hasChanges()) {
                    showDiscardChangesDialog();
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }
}
