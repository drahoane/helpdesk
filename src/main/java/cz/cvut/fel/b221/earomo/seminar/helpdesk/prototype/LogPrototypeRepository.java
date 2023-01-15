package cz.cvut.fel.b221.earomo.seminar.helpdesk.prototype;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Log;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.LogType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class LogPrototypeRepository {
    private static volatile LogPrototypeRepository instance;
    private Map<LogType, Log> repository = new HashMap<>();

    private LogPrototypeRepository() {
        Log authLog = new Log();
        authLog.setAction(LogType.FAILED_LOGIN);
        authLog.setDescription("Failed login attempt");
        authLog.setCreatedAt(LocalDateTime.now());

        Log unauthorizedLog = new Log();
        unauthorizedLog.setAction(LogType.UNAUTHORIZED);
        unauthorizedLog.setDescription("Insufficient permissions");
        unauthorizedLog.setCreatedAt(LocalDateTime.now());

        Log createdLog = new Log();
        createdLog.setAction(LogType.CREATE);
        createdLog.setDescription("Resource created");
        createdLog.setCreatedAt(LocalDateTime.now());

        Log updatedLog = new Log();
        updatedLog.setAction(LogType.UPDATE);
        updatedLog.setDescription("Resource updated");
        updatedLog.setCreatedAt(LocalDateTime.now());

        Log deletedLog = new Log();
        deletedLog.setAction(LogType.DELETE);
        deletedLog.setDescription("Resource deleted");
        deletedLog.setCreatedAt(LocalDateTime.now());

        repository.put(LogType.FAILED_LOGIN, authLog);
        repository.put(LogType.UNAUTHORIZED, unauthorizedLog);
        repository.put(LogType.CREATE, createdLog);
        repository.put(LogType.UPDATE, updatedLog);
        repository.put(LogType.DELETE, deletedLog);
    }

    public static LogPrototypeRepository getInstance() {
        if(instance == null) {
            synchronized (LogPrototypeRepository.class) {
                if(instance == null) {
                    instance = new LogPrototypeRepository();
                }
            }
        }

        return instance;
    }

    public Log getPrototype(LogType logType) {
        return repository.get(logType).copy();
    }
}
