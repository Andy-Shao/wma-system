package com.andyshao.application.wma.controller;

import com.andyshao.application.wma.controller.domain.GroupInfo;
import com.andyshao.application.wma.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/8/16
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @GetMapping("/getById/{id}")
    @ResponseBody
    public Mono<GroupInfo> findById(@PathVariable("id") String uuid) {
        return this.groupService.findById(uuid, null);
    }

    @PutMapping("/addMaterial")
    @ResponseBody
    public Mono<Void> addMaterial(@RequestParam("groupId")String groupId, @RequestParam("materialId")String materialId) {
        return this.groupService.addMaterial(groupId, materialId, null);
    }

    @DeleteMapping("/removeMaterial")
    @ResponseBody
    public Mono<Void> removeMaterial(@RequestParam("groupId")String groupId, @RequestParam("materialId")String materialId) {
        return this.groupService.removeMaterial(groupId, materialId, null);
    }
}
