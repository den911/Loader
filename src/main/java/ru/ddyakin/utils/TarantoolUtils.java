package ru.ddyakin.utils;

import ru.ddyakin.enums.KeyCommonField;
import ru.ddyakin.enums.StructField;
import ru.ddyakin.jdo.KeyCommon;
import ru.ddyakin.jdo.Structure;

import java.util.Date;
import java.util.List;

@SuppressWarnings("unchecked")
public class TarantoolUtils {
    public static Integer getInteger(List fields, int index) {
        Number val = getNumber(fields, index);
        return val==null? null: val.intValue();
    }

    public static Long getLong(List fields, int index) {
        Number val = getNumber(fields, index);
        return val==null? null: val.longValue();
    }

    public static Double getDouble(List fields, int index) {
        Number val = getNumber(fields, index);
        return val==null? null: val.doubleValue();
    }

    public static Float getFloat(List fields, int index) {
        Number val = getNumber(fields, index);
        return val==null? null: val.floatValue();
    }

    public static Date getDate(List fields, int index) {
        Long v = getLong(fields, index);
        return v==null? null: new Date(v);
    }

    public static String getString(List fields, int index) {
        return (fields!=null && index<fields.size())? (String)fields.get(index): null;
    }

    public static List<List> getTarantoolManyResults(List res) {
        if (res==null || res.isEmpty())
            return null;
        List resLst = (List)res.get(0);
        if (resLst==null || resLst.isEmpty())
            return null;
        return (List<List>)resLst;
    }

    public static List getTarantoolUniqueResult(List res) {
        List<List> mres = getTarantoolManyResults(res);
        if (mres==null || mres.isEmpty())
            return null;
        List fields = mres.get(0);
        return (fields==null || fields.isEmpty())? null: fields;
    }

    private static Number getNumber(List fields, int index) {
        return (fields!=null && index<fields.size())? (Number)fields.get(index): null;
    }

    public static Structure convertRow(List spaces) {
        Structure structure = new Structure();
        structure.setId(getInteger(spaces, StructField.ID.id));
        structure.setName(getString(spaces, StructField.NAME.id));
        structure.setStatus(getString(spaces, StructField.STATUS.id));
        structure.setRecordsNumber(getLong(spaces, StructField.RECORDS.id));
        return structure;
    }

    public static KeyCommon convertKey(List keys) {
        KeyCommon key = new KeyCommon();
        key.setId(getInteger(keys, KeyCommonField.ID.id));
        key.setTimestamp(getLong(keys, KeyCommonField.TIMESTAMP.id));
        return key;
    }
}
