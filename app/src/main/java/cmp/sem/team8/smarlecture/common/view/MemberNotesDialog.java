package cmp.sem.team8.smarlecture.common.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.model.NoteModel;

/**
 * Created by AmmarRabie on 28/04/2018.
 */

/**
 * Custom dialog for providing <B>View</B> of:
 * Displaying one member notes.
 * Add new note.
 * Delete existing one
 */
public class MemberNotesDialog extends DialogFragment {

    private NotesListAdapter notesAdapter;
    private MemberNotesDialogListener mDialogListener;


    public MemberNotesDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static MemberNotesDialog newInstance(String title, MemberNotesDialogListener dialogListener, ArrayList<NoteModel> notes) {
        MemberNotesDialog frag = new MemberNotesDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putSerializable("listener", dialogListener);
        args.putSerializable("notes", notes);
        frag.setArguments(args);
        return frag;
    }

    public void addNote(NoteModel note) {
        notesAdapter.add(note);
        notesAdapter.notifyDataSetChanged();
    }

    public void deleteNote(String noteId) {
        for (int i = 0; i < notesAdapter.getCount(); i++)
            if (notesAdapter.getItem(i).getId().equals(noteId))
                notesAdapter.remove(notesAdapter.getItem(i));
        notesAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_member_notes, container);
    }

    @Override
    public void onViewCreated(View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        ListView mNotesList = root.findViewById(R.id.memberNotesDialog_list);
        final TextView newNoteText = root.findViewById(R.id.memberNotesDialog_newNote);
        View saveView = root.findViewById(R.id.memberNotesDialog_saveNote);

        String title = getArguments().getString("title", "Enter Name");
        mDialogListener = ((MemberNotesDialogListener) getArguments().getSerializable("listener"));
        ArrayList<NoteModel> notes = (ArrayList<NoteModel>) getArguments().getSerializable("notes");

        getDialog().setTitle(title);
        notesAdapter = new NotesListAdapter(getActivity(), notes);
        mNotesList.setAdapter(notesAdapter);
        saveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogListener.onNewNoteAdded(newNoteText.getText().toString());
            }
        });
    }


    public interface MemberNotesDialogListener extends Serializable {
        void onDeleteNoteClicked(NoteModel noteClicked);

        void onNewNoteAdded(String noteText);
    }

    private class NotesListAdapter extends ArrayAdapter<NoteModel> {
        public NotesListAdapter(Activity context, ArrayList<NoteModel> notes) {
            super(context, 0, notes);
        }

        /**
         * Provides a view for an AdapterView (ListView, GridView, etc.)
         *
         * @param position    The position in the list of data that should be displayed in the
         *                    list item view.
         * @param convertView The recycled view to populate.
         * @param parent      The parent ViewGroup that is used for inflation.
         * @return The View for the position in the AdapterView.
         */
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Check if the existing view is being reused, otherwise inflate the view
            View listItemView = convertView;
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.item_member_note, parent, false);
            }

            // Get the {@link AndroidFlavor} object located at this position in the list
            NoteModel currNote = getItem(position);

            ((TextView) listItemView.findViewById(R.id.memberNoteItem_text)).setText(currNote.getText());
            listItemView.findViewById(R.id.memberNoteItem_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialogListener.onDeleteNoteClicked(getItem(position));
                }
            });

            // Return the whole list item layout (containing 2 TextViews and an ImageView)
            // so that it can be shown in the ListView
            return listItemView;
        }


    }
}
