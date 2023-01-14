package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Log;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.User;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.LogType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.prototype.LogPrototypeRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.LogRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    private final LogRepository logRepository;
    private final LogPrototypeRepository logPrototypeRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
        this.logPrototypeRepository = LogPrototypeRepository.getInstance();
    }

    public Log createLogByTemplate(@NotNull LogType type, @NotNull User user, @NotNull Class resource, String ip) {
        Log log = logPrototypeRepository.getPrototype(type);
        log.setUser(user);
        log.setResource(resource);
        log.setIp(ip);

        return logRepository.save(log);
    }

}
