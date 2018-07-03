package net.machina.wirtualnauczelnia.network;

public interface OnNetworkDataReceivedListener {
    void onDataReceived(boolean isSuccessful, String data);
}