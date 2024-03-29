package cs.usfca.edu.edgex.apis.flowapis;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import cs.usfca.edu.edgex.exceptions.EventNotFoundException;
import cs.usfca.edu.edgex.exceptions.FlowNotFoundException;
import cs.usfca.edu.edgex.model.FlowIdModel;
import cs.usfca.edu.edgex.model.FlowModel;
import cs.usfca.edu.edgex.utils.ApiInputMode;

@Controller
@RequestMapping("/flow")
public class FlowAPIs {
	// TODO : Don't re-execute the flow if it's activated once
	@PostMapping(value = "/add", consumes = "application/json")
	@ResponseBody()
	public ResponseEntity<?> registerFlow(@RequestBody FlowModel flowModel) {
		try {
			String resp = FlowHandlers.addFlow(flowModel, ApiInputMode.IndividualAPI);
			FlowIdModel fidM = new FlowIdModel();
			fidM.setFlowId(resp);
			return ResponseEntity.status(HttpStatus.OK).body(fidM);
		}
		catch(EventNotFoundException | FlowNotFoundException ef) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, ef.getMessage(), null);
		}
	}
	
	@PostMapping(value = "/activate", consumes = "application/json")
	@ResponseBody()
	public ResponseEntity<?> activateFlow(@RequestBody FlowIdModel flowIdModel) {
		try {
			FlowHandlers.activateFlow(flowIdModel.getFlowId());
			return ResponseEntity.status(HttpStatus.OK).body("flow " + flowIdModel.getFlowId() + " activated successfully");
		} catch (FlowNotFoundException e) {
			return ResponseEntity.status(HttpStatus.OK).body("flow " + flowIdModel.getFlowId() + " does not exist in our database");
		}
	}
	
	@PostMapping(value = "/deactivate", consumes = "application/json")
	@ResponseBody()
	public ResponseEntity<?> deactivateFlow(@RequestBody FlowIdModel flowIdModel) throws FlowNotFoundException {
		try {
			FlowHandlers.deactivateFlow(flowIdModel.getFlowId());
			return ResponseEntity.status(HttpStatus.OK).body("flow " + flowIdModel.getFlowId() + " deactivated successfully");
		} catch (FlowNotFoundException e) {
			return ResponseEntity.status(HttpStatus.OK).body("flow " + flowIdModel.getFlowId() + " does not exist in our database");
		}
	}
	
	@PostMapping(value = "/delete", consumes = "application/json")
	@ResponseBody()
	public ResponseEntity<?> deleteFlow(@RequestBody FlowIdModel flowIdModel) {
		try {
			FlowHandlers.deleteFlow(flowIdModel.getFlowId());
			return ResponseEntity.status(HttpStatus.OK).body("flow " + flowIdModel.getFlowId() + " deleted successfully");
		} catch (FlowNotFoundException e) {
			return ResponseEntity.status(HttpStatus.OK).body("flow " + flowIdModel.getFlowId() + " does not exist in our database");
		}
	}
	
	@GetMapping(value = "/listAllFlowIds")
	@ResponseBody()
	public List<String> listAllFlowIds() {
		return FlowHandlers.listAllFlowIds();
	}
	
	@GetMapping(value = "/listActiveFlowIds")
	@ResponseBody()
	public List<String> listActiveFlowIds() {
		return FlowHandlers.listAllActiveFlowIds();
	}
}
