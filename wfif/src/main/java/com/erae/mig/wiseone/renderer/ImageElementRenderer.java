package com.erae.mig.wiseone.renderer;

import java.util.HashMap;
import java.util.Map;

import com.erae.mig.wiseone.model.ChildElementBean;
import com.erae.mig.wiseone.model.ImageElementBean;

public class ImageElementRenderer extends ChildElementRenderer {
	
	public String renderElement(ChildElementBean elem, Object data) {
		if (elem instanceof ImageElementBean)
			return renderElement((ImageElementBean) elem, data);
		return super.renderElement(elem, data);
	}

	public String renderElement(ImageElementBean elem, Object data) {

		
		StringBuffer buffer = new StringBuffer("<div");
		
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put("wo_elem_id", elem.getId());
		appendAttributes(buffer, attrMap);
				
		Map<String, Object> styleAttrMap = new HashMap<String, Object>();

		styleAttrMap.putAll(getLayoutStyleAttribute(elem));
		
		styleAttrMap.put("background-color", elem.getBackgroundColor().toRGBString());
		styleAttrMap.put("border", elem.getBorderWidth() + "px solid " + elem.getBorderColor().toRGBString());
		appendStyleAttribute(buffer, styleAttrMap);
		buffer.append(">");
		buffer.append("<img src=\"").append(elem.getSource()).append("\" />");
		buffer.append("</div>");
		return buffer.toString();
	}


}
