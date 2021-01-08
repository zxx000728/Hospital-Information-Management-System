package com.hims.serviceImpl;

import com.hims.domain.Bed;
import com.hims.repository.BedRepository;
import com.hims.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BedServiceImpl implements BedService {
    @Autowired
    private BedRepository bedRepository;
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    public BedServiceImpl(BedRepository bedRepository, UserServiceImpl userService) {
        this.bedRepository = bedRepository;
        this.userService = userService;
    }

    public Map<String, Object> getBedDataPanelByHNurseId(String id) {
        Map<String, Object> map = new HashMap<>();
        List<Integer> wards = userService.findWardIdByHeadNurseId(Integer.parseInt(id));
        List<Bed> beds = new ArrayList<>();
        for (Integer integer : wards) {
            List<Bed> occupied = bedRepository.findOccupiedByWardId(integer);
            if (occupied != null) {
                beds.addAll(occupied);
            }
            List<Bed> free = bedRepository.findFreeByWardId(integer);
            if (free != null) {
                beds.addAll(free);
            }
        }
        map.put("bed", beds);
        return map;
    }
}
