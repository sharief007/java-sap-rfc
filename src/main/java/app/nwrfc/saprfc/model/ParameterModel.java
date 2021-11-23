package app.nwrfc.saprfc.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ParameterModel implements Serializable {
    Map<String,Object> imports = new HashMap<>();
    Map<String,Object> changing = new HashMap<>();
}
