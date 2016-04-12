package com.erae.mig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.erae.mig.approval.ApprovalBuilder;
import com.erae.mig.common.Log;
import com.erae.mig.util.CommonUtil;

public class MigBuilder {

	private static String STARTDATE = "";
	private static String ENDDATE = "";

	public static void main(String[] args) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		ssss
		try {
			if (args.length != 2) {
				Log.log(" Please check parameters ! ]", Log.ERROR);
			} else {
				STARTDATE = args[0] + "01";
				ENDDATE = args[1];

				if (CommonUtil.isNull(STARTDATE) && CommonUtil.isNull(ENDDATE)) {
					Log.log(" Migration Start date or End date value is null ! ]", Log.ERROR);
				} else if (!CommonUtil.isDate(STARTDATE) || !CommonUtil.isDate(ENDDATE)) {
					Log.log(" Migration Start date or End date is invalid type ! ]", Log.ERROR);
				} else {
					Date sDate = sdf.parse(STARTDATE);
					Date eDate = sdf.parse(ENDDATE);

					if (eDate.before(sDate)) {
						Log.log(" Migration Start date is earlier than the end date ! ]", Log.ERROR);
					} else {
						MigBuilder builder = new MigBuilder();
						builder.runMig();
					}
				}
			}
		} catch (Exception e) {
			Log.log(" Migration Start date or End date value is null ! ]", Log.ERROR);
		}
	}

	public void runMig() {
		Log.log("[ WF Migration job Started !! at " + System.currentTimeMillis() + "  ] " + STARTDATE
				+ " ~ " + ENDDATE, Log.INFO);
		try {
			Map<String, String> params = new HashMap<String,String>();
			params.put("start_date", STARTDATE);
			params.put("end_date", ENDDATE);
			
		    ApprovalBuilder builder = new ApprovalBuilder();
			builder.migrationDocuments(params);

		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.log("[ WF Migration job End !! at " + System.currentTimeMillis() + "  ]", Log.INFO);
	}

}
