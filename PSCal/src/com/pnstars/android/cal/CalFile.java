package com.pnstars.android.cal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;

import com.pnstars.android.helper.PSDbg;

public class CalFile {
	private static final int LAST_VERSION = 2;
	private static final String CAL_FILE = "pscal.data";
	private Context mContext;
	private ArrayList<CalFile.FileOp> mFOp;
	
	public CalFile (Context context) {
		mContext = context;
		mFOp = new ArrayList<CalFile.FileOp>();
		
//		mFOp.add(logic);
//		mFOp.add(display);
//		mFOp.add(history);
	}
	
	public void register (FileOp obj) {
		mFOp.add(obj);
	}
	
	public interface FileOp {
		void load(int version, DataInput in) throws IOException ;
		void save(DataOutput out) throws IOException ;
	}
	
		
	public void load() {
		PSDbg.d("load()");
		try {
			InputStream is = new BufferedInputStream(mContext.openFileInput(CAL_FILE), 8192);
			DataInputStream in = new DataInputStream(is);
			int version = in.readInt();
			if (version > LAST_VERSION) {
				in.close();
				throw new IOException("Data version " + version + "; expected " + LAST_VERSION);
			}
			for (FileOp op : mFOp) {
				op.load(version, in);
			}
			in.close();
		} catch (FileNotFoundException e) {
			PSDbg.d("" + e);
		} catch (IOException e) {
			PSDbg.d("" + e);
		}
	}
	
	public void save() {
		PSDbg.d("save()");
		try {
			OutputStream os = new BufferedOutputStream(mContext.openFileOutput(CAL_FILE, 0), 8192);
			DataOutputStream out = new DataOutputStream(os);
			out.writeInt(LAST_VERSION);
			for (FileOp op : mFOp) {
				op.save(out);
			}
			out.close();
		} catch (IOException e) {
			PSDbg.d("" + e);
		}
	}
}
