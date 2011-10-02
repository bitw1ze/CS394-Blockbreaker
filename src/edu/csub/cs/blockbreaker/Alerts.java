package edu.csub.cs.blockbreaker;

import android.app.AlertDialog;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.content.DialogInterface;

public class Alerts {
	public Alerts(String title, String message, int layout, Activity activity) {
		LayoutInflater li = LayoutInflater.from(activity);
		View view = li.inflate(layout, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setView(view);

		PromptListener pl = new PromptListener(view, activity);
		builder.setPositiveButton("OK", pl);
		builder.setNegativeButton("Cancel", pl);

		AlertDialog ad = builder.create();
		ad.show();
	}
	
	public class PromptListener implements DialogInterface.OnClickListener {
		private String promptReply = null;
		
		View promptDialogView = null;
		Activity activity = null;
		
		public PromptListener(View inDialogView, Activity a) {
			promptDialogView = inDialogView;
			activity = a;
		}
		
		public void onClick(DialogInterface v, int buttonId) {
			if (buttonId == DialogInterface.BUTTON_POSITIVE){
				promptReply = getPromptText();
			}
			else {
				promptReply = null;
			}
			activity.finish();
		}
		
		private String getPromptText() {
			EditText et = (EditText)promptDialogView.findViewById(R.id.editText_prompt);
			return et.getText().toString();
		}
		public String getPromptReply() { return promptReply; }
	}
}
