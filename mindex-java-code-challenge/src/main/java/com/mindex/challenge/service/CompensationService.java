package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;
import java.util.*;

public interface CompensationService {
    Compensation create(Compensation compensation);
    Compensation read(String id);
	List<Compensation> readAll();
}
