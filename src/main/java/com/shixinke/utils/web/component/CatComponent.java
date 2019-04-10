package com.shixinke.utils.web.component;

import com.dianping.cat.Cat;
import com.shixinke.utils.web.config.CatConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author shixinke
 * crated 19-4-9 下午4:04
 * @version 1.0
 */
@Component
@Slf4j
public class CatComponent  {

    private boolean catEnabled;

    @Autowired
    private CatConfig catConfig;

    public CatComponent() {
        catEnabled = catConfig.getEnable();
        if (catEnabled) {
            Cat.enable();
        } else {
            Cat.disable();
        }
    }



    public void logBatchTransaction(String type, String name, int count, int error, long sum) {
        if (!catEnabled) {
            log.debug("[CatLogger] [logBatchTransaction] type = {}, name = {}, count = {}, error = {}, sum = {}");
            return;
        }
        Cat.logBatchTransaction(type, name, count, error, sum);
    }

    public void logError(String message, Throwable cause) {
        if (!catEnabled) {
            log.error("[CatLogger] message = {}, exception = {}", message, cause);
            return;
        }
        Cat.logError(message, cause);
    }

    public void logError(Throwable cause) {
        if (!catEnabled) {
            log.error("[CatLogger] exception = {}", cause);
            return;
        }
        Cat.logError(cause);
    }

    public void logErrorWithCategory(String category, String message, Throwable cause) {
        if (!catEnabled) {
            log.error("[CatLogger] category = {}, message = {}, exception = {}", category, message, cause);
            return;
        }
        Cat.logErrorWithCategory(category, message, cause);
    }

    public void logErrorWithCategory(String category, Throwable cause) {
        if (!catEnabled) {
            log.error("[CatLogger] category = {}, exception = {}", category,  cause);
            return;
        }
        Cat.logErrorWithCategory(category, cause);
    }

    public void logEvent(String type, String name) {
        if (!catEnabled) {
            log.warn("[CatLogger] type = {}, exception = {}", type, name);
            return;
        }
        Cat.logEvent(type, name);

    }

    public void logEvent(String type, String name, String status, String nameValuePairs) {
        if (!catEnabled) {
            log.warn("[CatLogger] type = {}, name={}, status ={},  nameValuePairs = {}", type, name, status, nameValuePairs);
            return;
        }
        Cat.logEvent(type, name, status, nameValuePairs);
    }

    public void logMetricForCount(String name) {
        logMetricForCount(name, (Map)null);
    }

    public void logMetricForCount(String name, int quantity) {
        logMetricForCount(name, quantity, (Map)null);
    }

    public void logMetricForCount(String name, int quantity, Map<String, String> tags) {
        if (!catEnabled) {
            log.trace("[CatLogger] name={}, quantity={}, tags = {}", name, quantity, tags);
            return;
        }
        Cat.logMetricForCount(name, quantity, tags);
    }

    public void logMetricForCount(String name, Map<String, String> tags) {
        if (!catEnabled) {
            log.trace("[CatLogger] name={}, tags = {}", name, tags);
            return;
        }
        Cat.logMetricForCount(name, tags);
    }

    public void logMetricForDuration(String name, long durationInMillis) {
        logMetricForDuration(name, durationInMillis, (Map)null);
    }

    public void logMetricForDuration(String name, long durationInMillis, Map<String, String> tags) {
        if (!catEnabled) {
            log.trace("[CatLogger] name={},durationInMillis={}, tags = {}", name, durationInMillis, tags);
            return;
        }
        Cat.logMetricForDuration(name, durationInMillis, tags);
    }

    public void logRemoteCallClient(Cat.Context ctx) {
        if (!catEnabled) {
            log.trace("[CatLogger][remoteCallClient] context = {}", ctx);
            return;
        }
        Cat.logRemoteCallClient(ctx);
    }

    public void logRemoteCallClient(Cat.Context ctx, String domain) {
        if (!catEnabled) {
            log.trace("[CatLogger] [remoteCallClient] context = {}, domain = {}", ctx, domain);
            return;
        }
        Cat.logRemoteCallClient(ctx, domain);
    }

    public void logRemoteCallServer(Cat.Context ctx) {
        if (!catEnabled) {
            log.debug("[CatLogger] [remoteCallServer] , context = {}", ctx);
            return;
        }
        Cat.logRemoteCallServer(ctx);
    }
}

