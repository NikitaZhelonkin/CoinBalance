package ru.nikitazhelonkin.cryptobalance.data;


import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

import ru.nikitazhelonkin.cryptobalance.data.entity.ImportExportData;
import ru.nikitazhelonkin.cryptobalance.utils.L;

public class ExportManager {

    private Context mContext;
    private ObjectMapper mObjectMapper;

    @Inject
    public ExportManager(Context context) {
        mContext = context;
        mObjectMapper = new ObjectMapper();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Nullable
    private File getExportFile() {
        File rootDir = Environment.getExternalStorageDirectory();
        if (rootDir != null) {
            File appDir = new File(rootDir, "CoinBalance");
            appDir.mkdirs();
            return new File(appDir, "coin_balance_config.json");
        }
        return null;
    }

    public File exportData(ImportExportData data) {
        try {
            File file = getExportFile();
            if (file == null)
                return null;
            String json = mObjectMapper.writeValueAsString(data);
            return writeFile(file, json) ? file : null;
        } catch (JsonProcessingException e) {
            L.e(e);
        }
        return null;
    }

    public ImportExportData importData(Uri uri) {
        try {
            String json = readFromUri(uri);
            return mObjectMapper.readValue(json, ImportExportData.class);
        } catch (IOException e) {
            L.e(e);
        }
        return null;
    }


    private boolean writeFile(File file, String text) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(text);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                //ignore
            }
        }
        return false;
    }

    private String readFromUri(Uri uri) {
        try {
            InputStream inputStream = mContext.getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                return null;
            }
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            return total.toString();
        } catch (IOException e) {
            L.e(e);
        }
        return null;

    }
}
