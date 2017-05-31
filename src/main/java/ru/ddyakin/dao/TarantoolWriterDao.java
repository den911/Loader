package ru.ddyakin.dao;

import ru.ddyakin.jdo.*;

public interface TarantoolWriterDao {

    String processStructRequest(Struct struct);

    WriteResponse processWrite(WriteRequest request);

    UpdateResponse processUpdate(UpdateRequest request);

    RemoveResponse processRemove(RemoveRequest request);

}
