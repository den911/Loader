package ru.ddyakin.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ddyakin.enums.StructField;
import ru.ddyakin.jdo.Structure;
import ru.ddyakin.tarantool.sharding.ShardedTarantoolTemplateManager;
import ru.ddyakin.utils.TarantoolUtils;

import java.util.ArrayList;
import java.util.List;

import static ru.ddyakin.utils.TarantoolUtils.*;

@Service
public class LoaderLogic {
    private static final Logger log = LoggerFactory.getLogger(LoaderLogic.class);

    @Autowired
    ShardedTarantoolTemplateManager templateManager;

    public List<Structure> getRows() {
        List<List> spaces = TarantoolUtils.getTarantoolManyResults(templateManager.getSpaces());
        if (spaces == null)
            return null;
        List<Structure> rows = new ArrayList<>(spaces.size());
        for (List fields: spaces) {
            rows.add(convertRow(fields));
        }
        return rows;
    }

}
