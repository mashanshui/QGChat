/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.qgchat.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qgchat.R;
import com.example.qgchat.db.DBInviteMessage;
import com.hyphenate.chat.EMClient;

import org.litepal.crud.DataSupport;

import java.util.List;

public class NewFriendsMsgAdapter extends ArrayAdapter<DBInviteMessage> {

	private static final String TAG = "NewFriendsMsgAdapter";
	private Context context;

	public NewFriendsMsgAdapter(Context context, int textViewResourceId, List<DBInviteMessage> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.em_row_invite_msg, null);
			holder.avator = (ImageView) convertView.findViewById(R.id.avatar);
            holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.agreeBtn = (Button) convertView.findViewById(R.id.agree);
			holder.refuseBtn = (Button) convertView.findViewById(R.id.refuse);
			// holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final DBInviteMessage msg = getItem(position);
		if (msg != null) {
            holder.agreeBtn.setVisibility(View.GONE);
            holder.refuseBtn.setVisibility(View.GONE);


			holder.message.setText(msg.getReason());
			holder.name.setText(msg.getFrom());
			// holder.time.setText(DateUtils.getTimestampString(new
			// Date(msg.getTime())));
			if (msg.getStatus() == DBInviteMessage.BEAGREED) {
                holder.agreeBtn.setVisibility(View.GONE);
				holder.refuseBtn.setVisibility(View.GONE);
				holder.message.setText(context.getResources().getString(R.string.Has_agreed_to_your_friend_request));
			} else if (msg.getStatus() == DBInviteMessage.BEINVITEED || msg.getStatus() == DBInviteMessage.BEAPPLYED ) {
			    holder.agreeBtn.setVisibility(View.VISIBLE);
				holder.refuseBtn.setVisibility(View.VISIBLE);
				if(msg.getStatus() == DBInviteMessage.BEINVITEED){
					if (msg.getReason() == null) {
						// use default text
						holder.message.setText(context.getResources().getString(R.string.Request_to_add_you_as_a_friend));
					}
				}else if (msg.getStatus() == DBInviteMessage.BEAPPLYED) { //application to join group
					if (TextUtils.isEmpty(msg.getReason())) {
						holder.message.setText(context.getResources().getString(R.string.Apply_to_the_group_of) + msg.getGroupName());
					}
				}
				
				// set click listener
                holder.agreeBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // accept invitation
						Log.e(TAG, "onClick:------------ "+ msg.getFrom());
                        acceptInvitation(holder.agreeBtn, holder.refuseBtn, msg);
					}
                });
				holder.refuseBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// decline invitation
					    refuseInvitation(holder.agreeBtn, holder.refuseBtn, msg);
					}
				});
			} else {
				String str = "";
				int status = msg.getStatus();
                switch (status) {
                    case DBInviteMessage.AGREED:
                        str = context.getResources().getString(R.string.Has_agreed_to);
                        break;
                    case DBInviteMessage.REFUSED:
                        str = context.getResources().getString(R.string.Has_refused_to);
                        break;
                    default:
                        break;
                }
                holder.message.setText(str);
            }
        }

		return convertView;
	}

	/**
	 * accept invitation
	 *
	 * @param buttonAgree
	 * @param buttonRefuse
	 * @param msg
	 */
	private void acceptInvitation(final Button buttonAgree, final Button buttonRefuse, final DBInviteMessage msg) {
		final ProgressDialog pd = new ProgressDialog(context);
		String str1 = context.getResources().getString(R.string.Are_agree_with);
		final String str2 = context.getResources().getString(R.string.Has_agreed_to);
		final String str3 = context.getResources().getString(R.string.Agree_with_failure);
		pd.setMessage(str1);
		pd.setCanceledOnTouchOutside(false);
		pd.show();

		new Thread(new Runnable() {
			public void run() {
				// call api
				try {
					if (msg.getStatus() == DBInviteMessage.BEINVITEED) {//accept be friends
						EMClient.getInstance().contactManager().acceptInvitation(msg.getFrom());
					} else if (msg.getStatus() == DBInviteMessage.BEAPPLYED) { //accept application to join group
						EMClient.getInstance().groupManager().acceptApplication(msg.getFrom(), msg.getGroupId());
					}
                    msg.setStatus(DBInviteMessage.AGREED);
                    // update database
                    ContentValues values = new ContentValues();
                    values.put("status", msg.getStatus());
                    DataSupport.update(DBInviteMessage.class, values, msg.getId());
                    ((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							buttonAgree.setText(str2);
							buttonAgree.setBackgroundDrawable(null);
							buttonAgree.setEnabled(false);

							buttonRefuse.setVisibility(View.INVISIBLE);
						}
					});
				} catch (final Exception e) {
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(context, str3 + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});

				}
			}
		}).start();
	}

	/**
     * decline invitation
     *
     * @param buttonAgree
     * @param buttonRefuse
	 * @param msg
     */
    private void refuseInvitation(final Button buttonAgree, final Button buttonRefuse, final DBInviteMessage msg) {
        final ProgressDialog pd = new ProgressDialog(context);
        String str1 = context.getResources().getString(R.string.Are_refuse_with);
        final String str2 = context.getResources().getString(R.string.Has_refused_to);
        final String str3 = context.getResources().getString(R.string.Refuse_with_failure);
        pd.setMessage(str1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        new Thread(new Runnable() {
            public void run() {
                // call api
                try {
                    if (msg.getStatus() == DBInviteMessage.BEINVITEED) {//decline the invitation
                        EMClient.getInstance().contactManager().declineInvitation(msg.getFrom());
                    } else if (msg.getStatus() == DBInviteMessage.BEAPPLYED) { //decline application to join group
                        EMClient.getInstance().groupManager().declineApplication(msg.getFrom(), msg.getGroupId(), "");
                    }
                    msg.setStatus(DBInviteMessage.REFUSED);
                    // update database
                    ContentValues values = new ContentValues();
                    values.put("status", msg.getStatus());
                    DataSupport.update(DBInviteMessage.class, values, msg.getId());
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            buttonRefuse.setText(str2);
                            buttonRefuse.setBackgroundDrawable(null);
                            buttonRefuse.setEnabled(false);

                            buttonAgree.setVisibility(View.INVISIBLE);
                        }
                    });
                } catch (final Exception e) {
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(context, str3 + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }).start();
    }

	private static class ViewHolder {
		ImageView avator;
		TextView name;
		TextView message;
        Button agreeBtn;
		Button refuseBtn;
	}

}
