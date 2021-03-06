package com.uduakobongeren.simpletodo.adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.uduakobongeren.simpletodo.R;
import com.uduakobongeren.simpletodo.fragments.EditItemDialogFragment;
import com.uduakobongeren.simpletodo.activities.MainActivity;
import com.uduakobongeren.simpletodo.dao.ToDoItemsDBHelper;
import com.uduakobongeren.simpletodo.models.Priority;
import com.uduakobongeren.simpletodo.models.ToDoItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author Uduak Obong-Eren
 * @since 8/19/17.
 */

public class ToDoItemsAdapter extends ArrayAdapter<ToDoItem> {

    private Context appContext;
    private ToDoItemsDBHelper dao;
    private ToDoItemsAdapter self = this;
    private RadioGroup itemPriorityGroup;

    public ToDoItemsAdapter(Context context, ArrayList<ToDoItem> items, ToDoItemsDBHelper dbHelper) {
        super(context, 0, items);
        appContext = context;
        dao = dbHelper;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(appContext).inflate(R.layout.item_todoitem, parent, false);
        }

        final ToDoItem toDoItem = getItem(position);
        final MainActivity mainActivity = (MainActivity) appContext;
        itemPriorityGroup = convertView.findViewById(R.id.itemPriority);
        final Calendar myCalendar = Calendar.getInstance();
        final TextView itemDescTextView = convertView.findViewById(R.id.itemName);
        final CheckBox itemCompletedCheckBox = convertView.findViewById(R.id.itemCompleted);
        final EditText itemDate = convertView.findViewById(R.id.itemDate);
        final RadioGroup priorityGroup = convertView.findViewById(R.id.itemPriority);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String dateFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
                String newDate = sdf.format(myCalendar.getTime());

                if (toDoItem != null){
                    if (!toDoItem.getCompletionDate().equals(newDate)){
                        if (setItemCompletionDate(toDoItem, newDate)){

                            //Update toDoItem model
                            toDoItem.setCompletionDate(newDate);

                            //Update view
                            itemDate.setText(newDate);
                        }
                    }
                }
            }
        };


        /** Set onClickListener to edit toDoItem description **/
        itemDescTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Previous implementation that uses an Activity
                /*Intent editItemIntent = new Intent(mainActivity, EditItemActivity.class);
                String itemToBeEdited = itemDescTextView.getText().toString();
                editItemIntent.putExtra("EDIT_ITEM", itemToBeEdited);
                editItemIntent.putExtra("ITEM_POS", position);

                mainActivity.startActivityForResult(editItemIntent, REQUEST_CODE);*/

                if (toDoItem != null){
                    EditItemDialogFragment editItemDialogFragment = new EditItemDialogFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("itemDesc", toDoItem.getDescription());
                    bundle.putInt("itemId", (int) toDoItem.getId());
                    bundle.putInt("itemPos", position);

                    editItemDialogFragment.setArguments(bundle);
                    editItemDialogFragment.show( mainActivity.getSupportFragmentManager(), "Show_Edit_Item_Fragment");
                }
            }
        });


        /** Set onLongClickListener to delete toDoItem **/
        itemDescTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final ToDoItem victimItem = getItem(position);

                if (victimItem != null){
                    if (deleteItemFromDatabase(victimItem.getId())){
                        self.remove(victimItem);
                    }
                }
                return true;
            }
        });

        /** Listener to mark toDoItem as completed **/
        itemCompletedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    //Mark item as completed
                    if (toDoItem != null) {
                        if (toDoItem.isCompleted() == 0) {

                            if (setItemCompletedState(toDoItem, 1)){
                                //Update toDoItem model
                                toDoItem.setCompleted(1);

                                //Update toDoItem View
                                itemDescTextView.setPaintFlags(itemDescTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                                //Disable priority radio buttons
                                for (int i = 0; i < priorityGroup.getChildCount(); i++) {
                                    priorityGroup.getChildAt(i).setEnabled(false);
                                }
                            }
                        }
                    }
                } else {
                    if (toDoItem != null) {
                        if (toDoItem.isCompleted() == 1) {

                            if (setItemCompletedState(toDoItem, 0)){
                                //Update toDoItem model
                                toDoItem.setCompleted(0);

                                //Update toDoItem View
                                itemDescTextView.setPaintFlags(itemDescTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

                                //Enable priority radio buttons
                                for (int i = 0; i < priorityGroup.getChildCount(); i++) {
                                    priorityGroup.getChildAt(i).setEnabled(true);
                                }
                            }
                        }
                    }
                }
            }
        });


        /** Listener to set toDoItem's priority **/
        priorityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {

                RadioButton checkedRadioButton = priorityGroup.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                String priority;

                if (isChecked) {
                    if (toDoItem != null){
                        String priorityChoice = ((String) checkedRadioButton.getText()).toUpperCase();

                        switch(priorityChoice){
                            case "LOW":
                                priority = Priority.LOW.getLevel();
                                if (!toDoItem.getPriority().equals(priorityChoice)){

                                    if (dao.updateItemPriority(toDoItem, priority)){
                                        toDoItem.setPriority(priority);
                                        //Toast.makeText(appContext, "Priority updated successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                //Update color based on priority
                                priorityGroup.setBackgroundResource(R.color.priorityLow);
                                break;
                            case "MEDIUM":
                                priority = Priority.MEDIUM.getLevel();
                                if (!toDoItem.getPriority().equals(priorityChoice)){

                                    if (dao.updateItemPriority(toDoItem, priority)){
                                        toDoItem.setPriority(priority);
                                        //Toast.makeText(appContext, "Priority updated successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                //Update color based on priority
                                priorityGroup.setBackgroundResource(R.color.priorityMedium);
                                break;
                            case "HIGH":
                                priority = Priority.HIGH.getLevel();
                                if (!toDoItem.getPriority().equals(priorityChoice)){

                                    if (dao.updateItemPriority(toDoItem, priority)){
                                        toDoItem.setPriority(priority);
                                        //Toast.makeText(appContext, "Priority updated successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                //Update color based on priority
                                priorityGroup.setBackgroundResource(R.color.priorityHigh);
                                break;
                            default:
                                priority = Priority.LOW.getLevel();
                        }
                    }
                }
            }
        });


        /** Listener to set toDoItem's completion date **/
        itemDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(appContext, date, myCalendar .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        // Populate the data into the template view using the data object
        if (toDoItem != null){

            itemCompletedCheckBox.setChecked(toDoItem.isCompleted() == 1);
            itemDescTextView.setText(toDoItem.getDescription());

            if (toDoItem.isCompleted() == 1){
                itemDescTextView.setPaintFlags(itemDescTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                setStatePriorityRadioButtons(false);
            }
            else{
                itemDescTextView.setPaintFlags(itemDescTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                setStatePriorityRadioButtons(true);
            }

            switch(toDoItem.getPriority()){
                case "LOW":
                    RadioButton lowPriorityRadio = itemPriorityGroup.findViewById(R.id.lowPriority);
                    lowPriorityRadio.setChecked(true);
                    itemPriorityGroup.setBackgroundResource(R.color.priorityLow);
                    break;
                case "MEDIUM":
                    RadioButton mediumPriorityRadio = itemPriorityGroup.findViewById(R.id.mediumPriority);
                    mediumPriorityRadio.setChecked(true);
                    itemPriorityGroup.setBackgroundResource(R.color.priorityMedium);
                    break;
                case "HIGH":
                    RadioButton highPriorityRadio = itemPriorityGroup.findViewById(R.id.highPriority);
                    highPriorityRadio.setChecked(true);
                    itemPriorityGroup.setBackgroundResource(R.color.priorityHigh);
                    break;
                default:
                    itemPriorityGroup.setBackgroundResource(R.color.priorityLow);
                    break;
            }

            itemDate.setText(toDoItem.getCompletionDate());
        }

        return convertView;
    }

    private boolean deleteItemFromDatabase(long id){
        if (dao.deleteToDoItem(id)){
            Toast.makeText(appContext, "Item deleted successfully!", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private boolean setItemCompletedState(ToDoItem item, int pos){
        return dao.updateItemCompletedState(item, pos);
    }

    private boolean setItemCompletionDate(ToDoItem item, String date){
        return dao.updateItemCompletionDate(item, date);
    }

    private void setStatePriorityRadioButtons(boolean state){
        for (int i = 0; i < itemPriorityGroup.getChildCount(); i++) {
            itemPriorityGroup.getChildAt(i).setEnabled(state);
        }
    }

}
