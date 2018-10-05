package eventViewer;

import eventViewer.model.Event;
import eventViewer.config.ColorSetConfig;
import eventViewer.config.ColumnSetConfig;
import eventViewer.config.FilterSetConfig;
import eventViewer.dao.SettingDAO;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EventController {

    @Autowired
    FilterSetConfig fsConf;
    @Autowired
    ColumnSetConfig columnConf;
    @Autowired
    ColorSetConfig colorConf;
    @Autowired
    ApplicationContext ctx;
        
    @Value("${default.filter}")
    String currentFilter;
    String currentOrder;
    int refresh;
    String host, username, ncoms;
    EventService es;

    @RequestMapping(value = {"/index", "/"})
    public String index(@RequestParam(required = false) String errorMsg,
            Model model) {
        if (es != null) {
            return "redirect:/view";
        }
        if (errorMsg != null && !errorMsg.isEmpty()) {
            model.addAttribute("errmsg", errorMsg);
        }
        return "index";
    }

    @RequestMapping("/connect")
    public String connect(@RequestParam(required = true) String username,
            @RequestParam String password,
            @RequestParam(required = true) String host,
            @RequestParam(defaultValue = "4100") int port,
            @RequestParam(defaultValue = "NCOMS") String ncoms) {

        //TODO Check validate params
        // temporary simple check for NULL value
        if (username.isEmpty() || host.isEmpty()) {
            return "redirect:/";
        }

        // new OMNIbus(host, port, ncoms, username, password)
        // Get dao implementations
        SettingDAO dao = ctx.getBean(SettingDAO.class, host, port, ncoms, username, password);
        es = new EventService(dao);
        this.host = host;
        this.username = username;
        this.ncoms = ncoms;
        // Set params from external settings
        if (fsConf.isSet()) {
            es.setFilters(fsConf);
        }
        if (columnConf.isSet()) {
            es.setAdditionalColumns(columnConf);
        }
        if (colorConf.isSet()) {
            es.setColor(colorConf);
        }
        return "redirect:/view";
    }

    // List available fields the event server
    @RequestMapping("/columns")
    public String columns(Model model) {
    	model.addAttribute("columns", es.listColumns);
    	return "columns";
    }
    // List events by filter
    @RequestMapping("/view")
    public String view(@RequestParam(value = "query", required = false, defaultValue = "") String rawFilter,
            @RequestParam(required = false, defaultValue = "LastOccurrence desc") String order,
            @RequestParam(required = false, defaultValue = "") String group,
            @RequestParam(required = false, defaultValue = "300") String ref,
            Model model) {

        //TODO store current filter
        // Set default filter and order
        if (rawFilter.isEmpty()) {
            rawFilter = currentFilter;
        } else {
            currentFilter = rawFilter;
        }
        currentOrder = order;
        refresh = Integer.parseInt(ref);

        model.addAttribute("events", es.list(currentFilter, group, currentOrder));

        model.addAttribute("colors", es.severityColor);
        model.addAttribute("listFilters", es.listFilters);
        model.addAttribute("listFields", es.listFields);
        model.addAttribute("order", currentOrder);
        model.addAttribute("filter", currentFilter);
        model.addAttribute("count", es.count(currentFilter));
        model.addAttribute("refresh", refresh);
        model.addAttribute("listRef", es.listRefresh);
        setBaseValueToModel(model);

        return "view";
    }

    @RequestMapping("/detail")
    public String edit(@RequestParam int serial, @RequestParam boolean edit,
            Model model) {
        model.addAttribute("event", es.get(serial));
        model.addAttribute("classStr", es.classConv);
        setBaseValueToModel(model);
        model.addAttribute("view", true);
        if (!edit) {
        	model.addAttribute("listFields", es.listDetails);
        }

        return edit ? "edit" : "detail";
    }

    @RequestMapping("/modify")
    public String update(@Valid @ModelAttribute Event event, BindingResult result,
            @RequestParam(defaultValue = "false") boolean advchk,
            @RequestParam boolean view, Model model) {
        // Check result
        if (result.hasErrors()) {
            //Correct error and repeat
            setBaseValueToModel(model);
            model.addAttribute("view", false);
            return "edit";
        }
        // Action by event
        if (view) {
            es.update(event, advchk);
        } else {
            es.create(event);
        }

        return "redirect:/view";
    }

    @RequestMapping("/create")
    public String create(Event event, Model model) {
        event.initCustomEvent(4400, // Class (Generic)
                1, // Type (Problem)
                1, // Severity (Indeterminate)
                "TESTING:Generate:ChangeMe:ForYou:Need", // Identifier
                "Test event from generator");
        setBaseValueToModel(model);
        model.addAttribute("view", false);

        return "edit";
    }

    /**
     * Mass update event: Severity and Acknowledged
     *
     * @param eventserial[]
     * @param sev
     * @param ack
     * @param action
     * @return
     */
    @RequestMapping("/bulkedit")
    public String bulkedit(@RequestParam(defaultValue = "") String eventserial[],
            @RequestParam int sev,
            @RequestParam(defaultValue = "-1") int ack,
            @RequestParam String action) {

        if (eventserial.length != 0) {
            // Mass update/delete
            if (action.equalsIgnoreCase("Delete")) {
                es.massdelete(eventserial);
            } else if (action.equalsIgnoreCase("Update")) {
                es.massupdate(eventserial, ack, sev);
            }
        }
        return "redirect:/view";
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam int serial) {
        es.delete(serial);
        return "redirect:/view";
    }

    @RequestMapping("/disconnect")
    public String disconnect() {
        es = null;
        return "redirect:/";
    }

    @RequestMapping("/helpClass")
    public String helpClass(Model model) {
        model.addAttribute("classConv", es.classConv);
        return "helpclass";
    }

    private void setBaseValueToModel(Model model) {
        model.addAttribute("sevStr", es.severityConv);
        model.addAttribute("host", host);
        model.addAttribute("ncoms", ncoms);
        model.addAttribute("username", username);
    }
}
