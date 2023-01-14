package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceNotFoundException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Log;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.User;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.LogType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.prototype.LogPrototypeRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.LogRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Set<Log> findAllByType(LogType type) {
        return new HashSet<>(logRepository.getAllByAction(type));
    }

    public Log find(@NotNull Long id) {
        return logRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Log.class, id));
    }

}
