package com.first.board.global.mongodb;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class MongoUtil {
    public ObjectId ConvertStringToObjectId(final String StringId) {
        return new ObjectId(StringId);
    }
}
