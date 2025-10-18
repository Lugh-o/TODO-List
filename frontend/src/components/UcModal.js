import { refreshTaskList } from "../common/Utils.js";
import { TaskService } from "../Store.js";
import { globals } from "../Store.js";

export function showCreateModal() {
	const tint = document.querySelector(".tint");

	const ucModal = document.querySelector(".ucModalContainer");
	const ucModalInputs = ucModal.querySelectorAll(".ucTextInput");
	const ucModalTitle = ucModal.querySelector("h2");

	ucModalInputs.forEach((input) => (input.value = ""));
	ucModalTitle.innerText = "Create Task";
	globals.editingTask = null;

	tint.style.display = "block";
	ucModal.style.display = "flex";
}

export function showEditModal(task) {
	const readModal = document.querySelector(".readModalContainer");

	const ucModal = document.querySelector(".ucModalContainer");
	const ucModalInputs = ucModal.querySelectorAll(".ucTextInput");
	const ucModalTitle = ucModal.querySelector("h2");

	ucModalInputs[0].value = task.name;
	ucModalInputs[1].value = task.description;
	ucModalInputs[2].value = task.endDate.substring(0, 10);
	ucModalInputs[3].value = task.priority;
	ucModalInputs[4].value = task.category;
	ucModalInputs[5].value = task.status.toUpperCase();

	ucModalTitle.innerText = "Edit Task";
	globals.editingTask = task;

	readModal.style.display = "none";
	ucModal.style.display = "flex";
}

export function closeUcModal() {
	const ucModal = document.querySelector(".ucModalContainer");
	const tint = document.querySelector(".tint");

	tint.style.display = "none";
	ucModal.style.display = "none";
}

export function ucModalListener() {
	const ucModal = document.querySelector(".ucModalContainer");
	const ucModalInputs = ucModal.querySelectorAll(".ucTextInput");

	const ucModalForm = ucModal.querySelector("form");
	ucModalForm.addEventListener("submit", (event) => {
		event.preventDefault();

		const data = {
			name: ucModalInputs[0].value,
			description: ucModalInputs[1].value,
			endDate: ucModalInputs[2].value,
			priority: ucModalInputs[3].value,
			category: ucModalInputs[4].value,
			status: ucModalInputs[5].value,
		};

		if (globals.editingTask) {
			TaskService.update(globals.editingTask, data);
		} else {
			TaskService.create(data);
		}

		closeUcModal();
		refreshTaskList();
	});
}
