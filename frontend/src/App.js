import { loadTasksByPriority } from "./com.acelerazg/TaskLoader.js";
import { TaskService } from "./com.acelerazg/Store.js";
import { closeReadModal } from "./components/ReadModal.js";
import { closeDeleteModal } from "./components/DeleteModal.js";
import { closeUcModal, showCreateModal } from "./components/UcModal.js";
import { filterTasks } from "./com.acelerazg/Utils.js";
import { ucModalListener } from "./components/UcModal.js";

export default function App() {
	window.addEventListener("DOMContentLoaded", async function () {
		try {
			const response = await fetch("../dbMock/tasksByPriority.json");
			if (!response.ok)
				throw new Error(`Failed to fetch tasks: ${response.status}`);
			const tasks = await response.json();
			TaskService.constructor(tasks);
			loadTasksByPriority();
		} catch (error) {
			console.error("Error fetching JSON files:", error);
		}
	});

	document.addEventListener("DOMContentLoaded", () => {
		const today = new Date().toISOString().split("T")[0];
		document.getElementById("endDateForm").setAttribute("min", today);
	});

	addButtonEventListeners();
	ucModalListener();
}

function addButtonEventListeners() {
	const readModalReturn = document.querySelector(".readModal .return");
	readModalReturn.addEventListener("click", closeReadModal);

	const deleteModalReturn = document.querySelector(".deleteModal .cancel");
	deleteModalReturn.addEventListener("click", closeDeleteModal);

	const createNewTask = document.querySelector(
		".newTaskButtonContainer button"
	);
	createNewTask.addEventListener("click", showCreateModal);

	const ucModalReturn = document.querySelector(".controls .cancel");
	ucModalReturn.addEventListener("click", closeUcModal);

	const todoFilter = document.querySelector("#todoFilter button");
	todoFilter.addEventListener("click", (e) => {
		filterTasks(e.currentTarget, "TODO");
	});

	const doingFilter = document.querySelector("#doingFilter button");
	doingFilter.addEventListener("click", (e) => {
		filterTasks(e.currentTarget, "DOING");
	});

	const doneFilter = document.querySelector("#doneFilter button");
	doneFilter.addEventListener("click", (e) => {
		filterTasks(e.currentTarget, "DONE");
	});
}
