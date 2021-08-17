package com.andyshao.application.wma.controller;

import com.andyshao.application.wma.controller.domain.MaterialInfo;
import com.andyshao.application.wma.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/8/15
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@RestController
@RequestMapping("/material")
public class MaterialController {
    @Autowired
    private MaterialService materialService;

    @PostMapping("/addOrUpdate")
    @ResponseBody
    public Mono<MaterialInfo> saveOrUpdate(@Valid @RequestBody MaterialInfo materialInfo) {
        return this.materialService.saveOrUpdate(materialInfo, null);
    }

    @GetMapping("/getByWord/{w}")
    @ResponseBody
    public Flux<MaterialInfo> findMaterialByWord(@PathVariable("w") String word) {
        return this.materialService.matchMaterialByWord(word, null);
    }

    @GetMapping("/getById/{id}")
    public Mono<MaterialInfo> findById(@PathVariable("id")String uuid) {
        return this.materialService.findById(uuid, null);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<String> removeMaterial(@PathVariable("id")String materialId) {
        return this.materialService.removeMaterial(materialId, null);
    }
}
