package com.cmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class PrtgJsonResponse implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3783070908932446154L;

    private Prtg prtg = new Prtg();

    public PrtgJsonResponse(Map<String, String> vMap, String text) {
        if (vMap != null) {
            for (Map.Entry<String, String> entry : vMap.entrySet()) {
                prtg.result.add(prtg.new Result(entry.getKey(), entry.getValue()));
            }
        }
        if (StringUtils.isNotBlank(text)) {
            prtg.text = text;
        }
    }

	class Prtg implements Serializable {

	    /**
         *
         */
        private static final long serialVersionUID = 6108399921783541456L;

        private List<Result> result = new ArrayList<>();
        private String text;

	    class Result implements Serializable {
	        /**
             *
             */
            private static final long serialVersionUID = 7323051839142799389L;

            private String channel;
	        private String value;

	        public Result() {

	        }

	        public Result(String channel, String value) {
	            this.channel = channel;
	            this.value = value;
	        }

            public String getChannel() {
                return channel;
            }
            public void setChannel(String channel) {
                this.channel = channel;
            }
            public String getValue() {
                return value;
            }
            public void setValue(String value) {
                this.value = value;
            }
	    }

        public List<Result> getResult() {
            return result;
        }

        public void setResult(List<Result> result) {
            this.result = result;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
	}

    public Prtg getPrtg() {
        return prtg;
    }

    public void setPrtg(Prtg prtg) {
        this.prtg = prtg;
    }
}
