package com.leventsclone.leventsclone.controller.admin.service;

import com.leventsclone.leventsclone.controller.common.AuthenticatedData;
import com.leventsclone.leventsclone.data.request.ProductAddReq;
import com.leventsclone.leventsclone.data.use.AuthenticatedUse;
import com.leventsclone.leventsclone.data.use.EventUse;
import com.leventsclone.leventsclone.service.impl.EventProductSer;
import com.leventsclone.leventsclone.service.impl.EventSer;
import com.leventsclone.leventsclone.service.impl.ProductSer;
import com.leventsclone.leventsclone.service.impl.UserSer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/admin/events")
public class EventController {
    private final EventSer eventSer;
    private final EventProductSer eventProductSer;
    private final ProductSer productSer;

    private final AuthenticatedData authenticatedData;

    public EventController(EventSer eventSer, ProductSer productSer, EventProductSer eventProductSer, UserSer userSer, AuthenticatedData authenticatedData) {
        this.eventSer = eventSer;
        this.productSer = productSer;
        this.eventProductSer = eventProductSer;
        this.authenticatedData = authenticatedData;
    }

    private void handleAttribute(Model model,
                                 EventUse eventUse,
                                 String action,
                                      String type
    ) {
        model.addAttribute("event", eventUse);
        model.addAttribute("type", type);
        model.addAttribute("action", action);
    }



    @GetMapping("")
    public String getHome(Model model,
                          @RequestParam("detail")Optional<String> detail,
                          @RequestParam("update") Optional<String> updatePr,
                          @RequestParam("action")Optional<String> actionPr,
                          @RequestParam("state")Optional<String> state) {
        authenticatedData.authentication(model);
        String action = "screen";
        String type = null;
        EventUse eventUse = new EventUse();
        if(actionPr.isPresent()) {
            action = "edit";
            type = "add";
        }
        if(updatePr.isPresent()) {
            action = "edit";
            type = "update";
            eventUse = eventSer.getById(Long.valueOf(updatePr.orElseThrow()));
        }
        if(detail.isPresent()) {
            action = "detail";
            eventUse = eventSer.getById(Long.valueOf(detail.orElseThrow()));
            type = eventUse.getName();

            if(state.isPresent()) {
                String typeDetail = state.orElseThrow();
                if(state.orElseThrow().equals("use")) {
                    model.addAttribute("productAdd", eventSer.getAllProductById(Long.valueOf(detail.orElseThrow())));
                }else  {
                    model.addAttribute("productAdd", productSer.getAllUseEvent());
                }
                model.addAttribute("state", typeDetail);
            }else {
                model.addAttribute("productAdd", productSer.getAllUseEvent());
                model.addAttribute("state", "notUse");
            }
        }
        handleAttribute(model, eventUse, action, type);
        model.addAttribute("events", eventSer.getAll());
        return "admin/PageAdmin/Service/Event";
    }


    @DeleteMapping("/delete")
    public String delete(Model model,  @RequestBody Optional<List<Long>> list ) {
        authenticatedData.authentication(model);
        eventSer.delete(list.orElseThrow());
        model.addAttribute("events", eventSer.getAll());
        handleAttribute(model, new EventUse(),"screen", "screen");
        return "admin/PageAdmin/Service/Event :: #admin__screen";
    }

    @GetMapping("/form-add")
    public String goEdit(Model model) {
        authenticatedData.authentication(model);
        handleAttribute(model, new EventUse(),"edit", "add");
        return "admin/PageAdmin/Service/Event :: #admin__screen";
    }

    @GetMapping("/event-product/{id}/use")
    public String eventProductUse(Model model, @PathVariable Optional<Long> id) {
        authenticatedData.authentication(model);
        EventUse eventUse = eventSer.getById(id.orElseThrow());
        model.addAttribute("productAdd", eventSer.getAllProductById(id.orElseThrow()));
        model.addAttribute("state", "use");
        handleAttribute(model, eventUse,"detail", eventUse.getName());
        return "admin/PageAdmin/Service/Event :: #admin__screen";
    }
    @GetMapping("/event-product/{id}/unUse")
    public String eventProductNotUse(Model model, @PathVariable Optional<Long> id) {
        authenticatedData.authentication(model);
        EventUse eventUse = eventSer.getById(id.orElseThrow());
        model.addAttribute("productAdd", productSer.getAllUseEvent());
        model.addAttribute("state", "notUse");
        handleAttribute(model, eventUse,"detail", eventUse.getName());
        return "admin/PageAdmin/Service/Event :: #admin__screen";
    }

    @GetMapping("/form-update/{id}")
    public String goUpdate(Model model, @PathVariable Optional<Long> id) {
        authenticatedData.authentication(model);
        handleAttribute(model, eventSer.getById(id.orElseThrow()),"edit", "update");
        return "admin/PageAdmin/Service/Event :: #admin__screen";
    }

    @GetMapping("/form-detail/{id}")
    public String goDetail(Model model, @PathVariable Optional<Long> id) {
        authenticatedData.authentication(model);
        EventUse eventUse = eventSer.getById(id.orElseThrow());
        model.addAttribute("productAdd", productSer.getAllUseEvent());
        handleAttribute(model, eventUse,"detail", eventUse.getName());
        model.addAttribute("state", "notUse");
        return "admin/PageAdmin/Service/Event :: #admin__screen";
    }


    @PostMapping("/save")
    public String add(Model model, @RequestBody Optional<EventUse> eventUse) {
        authenticatedData.authentication(model);
        if(!eventSer.checkPath(eventUse.orElseThrow().getPath())) {
            model.addAttribute("error", "Trùng đường dẫn");
        } else  {
            eventSer.save(eventUse.orElseThrow());
            model.addAttribute("success", "Thêm sự kiện mới thành công");
        }
        handleAttribute(model, new EventUse(),"edit", "add");
        return "admin/PageAdmin/Service/Event :: #admin__screen";
    }


    @PutMapping("/update")
    public String update(Model model, @RequestBody Optional<EventUse> eventUse) {
        authenticatedData.authentication(model);
        EventUse eventUse1 = eventSer.getById(eventUse.orElseThrow().getId());
        String error = null;
        if(!Objects.equals(eventUse1.getPath(), eventUse.orElseThrow().getPath())) {
            if(!eventSer.checkPath(eventUse.orElseThrow().getPath())) {
                error = "Trùng đường dẫn";
            }
        }
        model.addAttribute("error", error);
        if(error == null) {
            eventSer.save(eventUse.orElseThrow());
            model.addAttribute("success", "Cập nhật kiện mới thành công");
        }
        handleAttribute(model, eventSer.getById(eventUse.orElseThrow().getId()),"edit", "add");
        return "admin/PageAdmin/Service/Event :: #admin__screen";
    }



    @PostMapping("/add-product-event/{id}")
    public String addProductsEvent(Model model, @RequestBody Optional<List<ProductAddReq>> list, @PathVariable("id") Optional<Long> idEvent) {
        authenticatedData.authentication(model);
        eventProductSer.save(list.orElseThrow(), idEvent.orElseThrow());
        EventUse eventUse = eventSer.getById(idEvent.orElseThrow());
        model.addAttribute("state", "notUse");
        handleAttribute(model, eventUse,"detail", eventUse.getName());
        model.addAttribute("productAdd", productSer.getAllUseEvent());
        return "admin/PageAdmin/Service/Event :: #admin__screen";
    }

    @DeleteMapping("/delete-product-event/{id}")
    public String deleteProductsEvent(Model model, @RequestBody Optional<List<ProductAddReq>> list, @PathVariable("id") Optional<Long> idEvent) {
        authenticatedData.authentication(model);
        eventProductSer.delete(list.orElseThrow(), idEvent.orElseThrow());
        EventUse eventUse = eventSer.getById(idEvent.orElseThrow());
        model.addAttribute("state", "use");
        handleAttribute(model, eventUse,"detail", eventUse.getName());
        model.addAttribute("productAdd", eventSer.getAllProductById(idEvent.orElseThrow()));
        return "admin/PageAdmin/Service/Event :: #admin__screen";
    }

    @PutMapping("/update-product-event/{id}")
    public String updateProductsEvent(Model model, @RequestBody Optional<List<ProductAddReq>> list, @PathVariable("id") Optional<Long> idEvent) {
        authenticatedData.authentication(model);
        eventProductSer.update(list.orElseThrow(), idEvent.orElseThrow());
        EventUse eventUse = eventSer.getById(idEvent.orElseThrow());
        model.addAttribute("state", "use");
        handleAttribute(model, eventUse,"detail", eventUse.getName());
        model.addAttribute("productAdd", eventSer.getAllProductById(idEvent.orElseThrow()));
        return "admin/PageAdmin/Service/Event :: #admin__screen";
    }


}























