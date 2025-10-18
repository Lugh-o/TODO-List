import { TaskService } from "./Store.js";
import { getPriorityColor } from "../common/Utils.js";
import { showReadModal } from "../components/ReadModal.js";

export function taskCard(task) {
	const priorityColor = getPriorityColor(task.priority);
	const card = document.createElement("div");
	card.className = "cardContainer";
	card.innerHTML = `
        <div class="column1">
            <h2 class="cardName">${task.name}</h2>
            <h3 class="cardCategory">Category: ${task.category}</h3>
        </div>
        <div class="column2">
            <div class="cardPriority" style="background-color: ${priorityColor};">${task.priority}</div>
            <div class="cardStatus">${task.status}</div>
        </div>
    `;
	card.addEventListener("click", () => showReadModal(task));
	return card;
}

export function loadTasksByPriority() {
	const listContainer = document.querySelector(".listContainer");
	TaskService.list.sort((a, b) => {
		const diff = parseInt(a.priority) - parseInt(b.priority);
		if (diff !== 0) return diff;
		return new Date(a.endDate) - new Date(b.endDate);
	});
	listContainer.innerHTML = "";
	TaskService.list.forEach((task) => {
		listContainer.appendChild(taskCard(task));
	});
}

export function loadTasksByStatus(status) {
	const listContainer = document.querySelector(".listContainer");
	const filteredTasks = TaskService.list.filter(
		(task) => task.status === status
	);
	listContainer.innerHTML = "";
	filteredTasks.forEach((task) => {
		listContainer.appendChild(taskCard(task));
	});
}
