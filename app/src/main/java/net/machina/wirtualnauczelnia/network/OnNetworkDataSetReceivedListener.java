package net.machina.wirtualnauczelnia.network;

import java.util.ArrayList;
import java.util.HashMap;

public interface OnNetworkDataSetReceivedListener {
    void onDatasetReceived(boolean isSuccessful, ArrayList<HashMap<String, String>> dataSet);
}
