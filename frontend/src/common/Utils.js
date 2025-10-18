import { loadTasksByPriority } from "../task/TaskLoader.js";
import { loadTasksByStatus } from "../task/TaskLoader.js";
import { globals } from "../Store.js";

export function getPriorityColor(priority) {
	return globals.colors[`Priority${parseInt(priority)}`];
}

export function refreshTaskList() {
	if (globals.selectedFilter) {
		loadTasksByStatus(globals.selectedFilter.innerText);
	} else {
		loadTasksByPriority();
	}
}

export function resetFilterButtonStyles() {
	const filterButtons = Array.from(
		document.querySelectorAll(".option button")
	);

	filterButtons.forEach((btn) => {
		btn.style.backgroundColor = globals.colors.ColumbiaBlue1;
		btn.style.color = globals.colors.Onyx;
	});
}

export function filterTasks(e, status) {
	const wasSelected = e === globals.selectedFilter;
	resetFilterButtonStyles();

	if (wasSelected) {
		globals.selectedFilter = null;
		loadTasksByPriority();
	} else {
		globals.selectedFilter = e;
		e.style.backgroundColor = globals.colors.PainesGray2;
		e.style.color = globals.colors.AliceBlue;
		loadTasksByStatus(status);
	}
}
