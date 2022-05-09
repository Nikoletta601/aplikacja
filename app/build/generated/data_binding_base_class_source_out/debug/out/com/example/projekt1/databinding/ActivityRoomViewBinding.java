// Generated by view binder compiler. Do not edit!
package com.example.projekt1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.projekt1.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRoomViewBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final TextView RoomName;

  @NonNull
  public final ImageView guzikBack;

  @NonNull
  public final ImageView guzikNotifications;

  @NonNull
  public final ImageView guzikSettings;

  @NonNull
  public final ImageView guzikTasks;

  @NonNull
  public final ImageView guzikUsers;

  @NonNull
  public final LinearLayout roomviewlayout;

  private ActivityRoomViewBinding(@NonNull RelativeLayout rootView, @NonNull TextView RoomName,
      @NonNull ImageView guzikBack, @NonNull ImageView guzikNotifications,
      @NonNull ImageView guzikSettings, @NonNull ImageView guzikTasks,
      @NonNull ImageView guzikUsers, @NonNull LinearLayout roomviewlayout) {
    this.rootView = rootView;
    this.RoomName = RoomName;
    this.guzikBack = guzikBack;
    this.guzikNotifications = guzikNotifications;
    this.guzikSettings = guzikSettings;
    this.guzikTasks = guzikTasks;
    this.guzikUsers = guzikUsers;
    this.roomviewlayout = roomviewlayout;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRoomViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRoomViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_room_view, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRoomViewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.RoomName;
      TextView RoomName = ViewBindings.findChildViewById(rootView, id);
      if (RoomName == null) {
        break missingId;
      }

      id = R.id.guzikBack;
      ImageView guzikBack = ViewBindings.findChildViewById(rootView, id);
      if (guzikBack == null) {
        break missingId;
      }

      id = R.id.guzikNotifications;
      ImageView guzikNotifications = ViewBindings.findChildViewById(rootView, id);
      if (guzikNotifications == null) {
        break missingId;
      }

      id = R.id.guzikSettings;
      ImageView guzikSettings = ViewBindings.findChildViewById(rootView, id);
      if (guzikSettings == null) {
        break missingId;
      }

      id = R.id.guzikTasks;
      ImageView guzikTasks = ViewBindings.findChildViewById(rootView, id);
      if (guzikTasks == null) {
        break missingId;
      }

      id = R.id.guzikUsers;
      ImageView guzikUsers = ViewBindings.findChildViewById(rootView, id);
      if (guzikUsers == null) {
        break missingId;
      }

      id = R.id.roomviewlayout;
      LinearLayout roomviewlayout = ViewBindings.findChildViewById(rootView, id);
      if (roomviewlayout == null) {
        break missingId;
      }

      return new ActivityRoomViewBinding((RelativeLayout) rootView, RoomName, guzikBack,
          guzikNotifications, guzikSettings, guzikTasks, guzikUsers, roomviewlayout);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
