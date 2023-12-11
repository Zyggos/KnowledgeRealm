package com.knowledgerealm.helpers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.knowledgerealm.MainActivity;
import com.knowledgerealm.QuestionActivity;
import com.knowledgerealm.R;

public class UiHelpers {

    /**
     * Shows a progress indicator with the loading message.
     *
     * @param context the current activity where the progress indicator will be displayed
     * @param message the loading message to be displayed
     * @return the progress indicator dialog
     */
    public static Dialog showProgressIndicator(Context context, String message) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
        );
        dialog.setCancelable(false);

        TextView progressText = dialog.findViewById(R.id.progress_text);
        progressText.setText(message);

        dialog.show();

        return dialog;
    }

    /**
     * Shows a progress indicator with a default loading message.
     *
     * @param context the current activity where the progress indicator will be displayed
     * @return the progress indicator dialog
     */
    public static Dialog showProgressIndicator(Context context) {
        return showProgressIndicator(context, null);
    }

    /**
     * An interface that is used to handle the user's response to the exit gameplay dialog.
     */
    public interface ExitGamePlayCallback {
        void onExitGamePlay(boolean exit);
    }

    /**
     * Shows a dialog that asks the user if they really want to leave the gameplay.
     *
     * @param questionActivity the current activity where the dialog will be displayed
     * @param callback         the callback that will be called when the user responds to the dialog
     */
    public static void showExitGamePlayDialog(final QuestionActivity questionActivity, final ExitGamePlayCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(questionActivity);
        builder.setTitle(questionActivity.getString(R.string.exit_gameplay));
        builder.setMessage(questionActivity.getString(R.string.are_you_sure_you_want_to_exit_gameplay));
        builder.setPositiveButton(questionActivity.getString(R.string.yes), (dialogInterface, i) -> callback.onExitGamePlay(true));
        builder.setNegativeButton(questionActivity.getString(R.string.no), (dialogInterface, i) -> callback.onExitGamePlay(false));
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * Shows a dialog that asks the user if they really want to leave the app.
     *
     * @param mainActivity the current activity where the dialog will be displayed
     */
    public static void showExitAppDialog(final MainActivity mainActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle(mainActivity.getString(R.string.exit_game));
        builder.setMessage(mainActivity.getString(R.string.are_you_sure_you_want_to_exit_game));
        builder.setPositiveButton(mainActivity.getString(R.string.yes), (dialogInterface, i) -> mainActivity.finishAffinity());
        builder.setNegativeButton(mainActivity.getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
