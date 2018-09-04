package org.explorer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.explorer.dto.PageListRequest;
import org.explorer.entity.JsonRpcSpec;
import org.explorer.util.GsonUtil;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.springframework.core.io.ClassPathResource;

/**
 * @author zacconding
 * @Date 2018-07-21
 * @GitHub : https://github.com/zacscoding
 */
public class Temp {

    @Test
    public void test() {
        try {
            Type listType = new TypeToken<ArrayList<JsonRpcSpec>>() {}.getType();
            Gson gson = GsonUtil.GsonFactory.createDefaultGson();
            List<JsonRpcSpec> specs = gson.fromJson(new InputStreamReader(new ClassPathResource("parity-json-spec.json").getInputStream()), listType);
            StringBuilder sb = new StringBuilder();

            for(JsonRpcSpec spec : specs) {
                spec.getMethods().forEach(m -> {
                    sb.append("final String ").append(m).append("=\"").append(m).append("\";\n");
                });
                sb.append("\n\n");
            }
            System.out.println(sb.toString());
        } catch (Exception e) {
            System.exit(-1);
        }
    }

}
