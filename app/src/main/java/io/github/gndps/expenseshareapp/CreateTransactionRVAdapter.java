package io.github.gndps.expenseshareapp;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Gundeep on 10/03/18.
 */

public class CreateTransactionRVAdapter extends RecyclerView.Adapter<CreateTransactionRVAdapter.CreateTransactionViewHolder> {

    private static final String TAG = CreateTransactionRVAdapter.class.getSimpleName();
    List<TransactionParticipant> participantList;
    private boolean mainActivityMode;

    public CreateTransactionRVAdapter(List<TransactionParticipant> participantList) {
        this.participantList = participantList;
    }

    public void updateParticipantList(List<TransactionParticipant> participantList) {
        this.participantList = participantList;
    }

    @Override
    public CreateTransactionRVAdapter.CreateTransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_add_transaction_item, parent, false);
        CreateTransactionViewHolder vh = new CreateTransactionViewHolder(view, new MyCustomEditTextListener(), new MyCustomEditTextListener2());
        return vh;
    }

    @Override
    public void onBindViewHolder(CreateTransactionViewHolder holder, int position) {
        if(participantList!=null && participantList.size()>0) {
            holder.myCustomEditTextListener.updatePosition(position);
            holder.myCustomEditTextListener2.updatePosition(position);
            holder.loadData(participantList.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        if(participantList!=null) {
            return participantList.size();
        } else {
            return 0;
        }
    }

    public void setMainActivityMode(boolean mainActivityMode) {
        this.mainActivityMode = mainActivityMode;
    }

    public List<TransactionParticipant> getParticipantList() {
        return participantList;
    }

    public class CreateTransactionViewHolder extends RecyclerView.ViewHolder {

        TransactionParticipant participant;
        int position;
        public TextView tvParticipant;
        public EditText etParticipantPaid;
        public EditText etParticipantShare;
        public Button buttonRemoveParticipant;
        public MyCustomEditTextListener myCustomEditTextListener;
        public MyCustomEditTextListener2 myCustomEditTextListener2;

        public CreateTransactionViewHolder(View v, MyCustomEditTextListener myCustomEditTextListener, MyCustomEditTextListener2 myCustomEditTextListener2) {
            super(v);
            this.myCustomEditTextListener = myCustomEditTextListener;
            this.myCustomEditTextListener2 = myCustomEditTextListener2;
            tvParticipant = v.findViewById(R.id.tv_participant);
            etParticipantPaid = v.findViewById(R.id.et_participant_paid);
            etParticipantShare = v.findViewById(R.id.et_participant_share);
            buttonRemoveParticipant = v.findViewById(R.id.button_remove_participant);
        }

        public void loadData(TransactionParticipant participant, int position) {
            if(mainActivityMode) {
                etParticipantShare.setVisibility(View.GONE);
                buttonRemoveParticipant.setVisibility(View.GONE);
                etParticipantPaid.setKeyListener(null);
            }
            this.participant = participant;
            this.position = position;
            tvParticipant.setText(participant.getName());
            if(participant.getPaid()>0) {
                etParticipantPaid.setText(participant.getPaid()+"");
            }
            if(participant.getShare()>0) {
                etParticipantShare.setText(participant.getShare()+"");
            }
            initChangeListeners();
        }

        private void initChangeListeners() {

            this.etParticipantPaid.addTextChangedListener(myCustomEditTextListener);
            this.etParticipantShare.addTextChangedListener(myCustomEditTextListener2);

            buttonRemoveParticipant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    participantList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            try {
                int paid = Integer.parseInt(charSequence.toString());
                TransactionParticipant par = participantList.get(position);
                par.setPaid(paid);
                participantList.set(position, par);
            } catch (Exception exception) {
                Log.e(TAG, "exception in change listener");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

    private class MyCustomEditTextListener2 implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            try {
                int shared = Integer.parseInt(charSequence.toString());
                TransactionParticipant par = participantList.get(position);
                par.setShare(shared);
                participantList.set(position, par);
            } catch (Exception exception) {
                Log.e(TAG, "exception in change listener");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

}
