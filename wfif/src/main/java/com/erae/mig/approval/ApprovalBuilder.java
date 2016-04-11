package com.erae.mig.approval;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.erae.mig.common.Log;
import com.erae.mig.common.MigConfig;
import com.erae.mig.common.SqlMapConfig;
import com.erae.mig.converter.ElementConverter;
import com.erae.mig.util.CommonUtil;
import com.erae.mig.util.DocumentUtil;
import com.erae.mig.util.FastViewElementRendererFactory;
import com.erae.mig.util.JSONGernerator;

public class ApprovalBuilder {

	public void migrationDocuments(Map<String, String> params) {
		try {
			List<Map<String,String>> sancList = getSancList(params);
			MigConfig migconfig = new MigConfig();

			for (Map<String,String> map : sancList) {
				writeMigDoc(map.get("str")+"", migconfig, map.get("docYear") +"/" + map.get("docMonth") + "/" + map.get("deptName") + ".txt");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private List<Map<String,String>> getSancList(Map<String, String> params) {
		try {
			List<Map<String,String>> sancList = new ArrayList<Map<String,String>>();
			String sanc_id;
			String form_seq;

			ElementConverter converter = new ElementConverter();
			converter.setFactory(new FastViewElementRendererFactory());

			List<Map<String, Object>> list = (List<Map<String, Object>>) SqlMapConfig.getSqlMapInstance().queryForList("getSancList", params);

			DocumentUtil util = new DocumentUtil();

			Map<String, String> param = new HashMap<String, String>();
			
			
			Log.log("[ list.size() : " + list.size() + "  ]", Log.INFO);
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) list.get(i);
				Map<String,String> data = new HashMap<String,String>();
				sanc_id = map.get("sourceId") + "";
				form_seq = map.get("form_seq") + "";

				util.setForm_seq(form_seq);
				util.setSanc_id(sanc_id);

				if (!CommonUtil.isNull(sanc_id)) {
					data.put("docYear",map.get("docYear")+"");
					data.put("docMonth",map.get("docMonth")+"");
					
					Map<String, Object> drafter = (Map<String, Object>) SqlMapConfig.getSqlMapInstance().queryForObject("getDrafter", sanc_id);
					map.put("drafter", drafter);
					data.put("deptName",drafter.get("")+"deptName");

					List<Map<String, Object>> receiver = (List<Map<String, Object>>) SqlMapConfig.getSqlMapInstance().queryForList("getReceiver", sanc_id);
					map.put("receiver", receiver);

					List<Map<String, Object>> activities = (List<Map<String, Object>>) SqlMapConfig.getSqlMapInstance().queryForList("getActivities", sanc_id);

					for (Map<String, Object> act : activities) {
						if (!CommonUtil.isNull(act.get("deputyUser"))) {
							param.put("wowf_sanc_id", sanc_id);
							param.put("deputyUser", act.get("deputyUser") + "");
							param.put("empNo", act.get("empNo") + "");
							Map<String, Object> deputyUser = (Map<String, Object>) SqlMapConfig.getSqlMapInstance().queryForObject("getDeputyUser", param);
							act.put("deputyUser", deputyUser);
						} else {
							act.put("deputyUser", new ArrayList<Map<String, String>>());
						}
					}

					map.put("activities", activities);

					Map<String, Object> receptionOrigin = (Map<String, Object>) SqlMapConfig.getSqlMapInstance().queryForObject("getOrignDocumentId", sanc_id);
					map.put("receptionOrigin", receptionOrigin);

					String str = converter.convertHTMLTag(util.makeDocumentMap());
					map.put("docBody", str);

					List<Map<String, String>> refList = util.getRefFiles();
					map.put("references", refList);

					map.put("readers", new ArrayList<Map<String, String>>());
					map.put("referrers", new ArrayList<Map<String, String>>());

					List<Map<String, Object>> attaches = (List<Map<String, Object>>) SqlMapConfig.getSqlMapInstance().queryForList("getAttaches", sanc_id);
					map.put("attaches", attaches);
				}
				
				data.put("str",JSONGernerator.getJSONStringObject(map));
				sancList.add(data);
			}
			return sancList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void writeMigDoc(String str, MigConfig migconfig, String file_name) {
		try {
			
			File file = new File(migconfig + File.separator + file_name);
		    file.mkdirs();
			
			BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
			String s = "addDocument " + str;
			out.write(s, 0, s.length());
			out.newLine();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
