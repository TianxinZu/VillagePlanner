// Generated by view binder compiler. Do not edit!
package com.villageplanner.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.villageplanner.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRemindersBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final Button addReminder;

  @NonNull
  public final LinearLayout reminders;

  private ActivityRemindersBinding(@NonNull ScrollView rootView, @NonNull Button addReminder,
      @NonNull LinearLayout reminders) {
    this.rootView = rootView;
    this.addReminder = addReminder;
    this.reminders = reminders;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRemindersBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRemindersBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_reminders, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRemindersBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.addReminder;
      Button addReminder = ViewBindings.findChildViewById(rootView, id);
      if (addReminder == null) {
        break missingId;
      }

      id = R.id.reminders;
      LinearLayout reminders = ViewBindings.findChildViewById(rootView, id);
      if (reminders == null) {
        break missingId;
      }

      return new ActivityRemindersBinding((ScrollView) rootView, addReminder, reminders);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
