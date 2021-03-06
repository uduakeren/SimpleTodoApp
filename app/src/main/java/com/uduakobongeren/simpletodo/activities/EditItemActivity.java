package com.uduakobongeren.simpletodo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.uduakobongeren.simpletodo.R;


/**
 * @author Uduak Obong-Eren
 * @since 8/13/17.
 */
public class EditItemActivity extends AppCompatActivity {

    private Button mSaveEditBtn;
    private EditText editTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        String itemToBeEdited = getIntent().getStringExtra("EDIT_ITEM");
        final int itemPos = getIntent().getIntExtra("ITEM_POS", -1);
        editTextField = (EditText) findViewById(R.id.editItem);
        mSaveEditBtn = (Button) findViewById(R.id.saveEdit);
        editTextField.setText(itemToBeEdited);

        mSaveEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent data = new Intent();
                data.putExtra("NEW_ITEM_VAL",  editTextField.getText().toString());
                data.putExtra("ITEM_POS", itemPos);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

}
