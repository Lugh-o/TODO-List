const colors = {
	AliceBlue: "#f0f6fa",
	ColumbiaBlue1: "#cee5f2",
	ColumbiaBlue2: "#accbe1",
	AirSuperiorityBlue: "#7c98b3",
	PainesGray1: "#637081",
	PainesGray2: "#536b78",
	Onyx: "#303538",

	Priority5: "#2e7d32",
	Priority4: "#9e9d24",
	Priority3: "#ffb300",
	Priority2: "#f4511e",
	Priority1: "#c62828",
};

/* -------------------- Initial Loading -------------------- */
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

/* -------------------- Data -------------------- */
const TaskService = {
	list: [],
	currentId: 1,

	constructor(tasks) {
		this.list = tasks;
		this.currentId = tasks.length + 1;
	},

	create(data) {
		const task = {
			id: this.currentId,
			name: data.name,
			description: data.description,
			endDate: data.endDate,
			priority: data.priority,
			category: data.category,
			status: data.status.toUpperCase(),
		};
		this.list.push(task);
		this.currentId++;

		return task;
	},

	update(task, data) {
		task.name = data.name;
		task.description = data.description;
		task.endDate = data.endDate;
		task.priority = data.priority;
		task.category = data.category;
		task.status = data.status.toUpperCase();
	},

	delete(task) {
		const index = this.list.indexOf(task);
		if (index !== -1) this.list.splice(index, 1);
	},
};

/* -------------------- UI -------------------- */
let selectedFilter = null;
const filterButtons = Array.from(document.querySelectorAll(".option button"));

function resetFilterButtonStyles() {
	filterButtons.forEach((btn) => {
		btn.style.backgroundColor = colors.ColumbiaBlue1;
		btn.style.color = colors.Onyx;
	});
}

function filterTasks(e, status) {
	const wasSelected = e === selectedFilter;
	resetFilterButtonStyles();

	if (wasSelected) {
		selectedFilter = null;
		loadTasksByPriority();
	} else {
		selectedFilter = e;
		e.style.backgroundColor = colors.PainesGray2;
		e.style.color = colors.AliceBlue;
		loadTasksByStatus(status);
	}
}

const tint = document.querySelector(".tint");
const readModal = document.querySelector(".readModalContainer");
const readModalStatus = readModal.querySelector(".cardStatus");
const readModalName = readModal.querySelector(".cardName");
const readModalDescription = readModal.querySelector(".cardDescription");
const readModalEndDate = readModal.querySelector(".cardEndDate");
const readModalCategory = readModal.querySelector(".cardCategory");
const readModalPriority = readModal.querySelector(".cardPriority");
const readModalEditButton = readModal.querySelector(".edit");
const readModalDeleteButton = readModal.querySelector(".delete");

let readModalEditListener, readModalDeleteListener;

const ucModal = document.querySelector(".ucModalContainer");
const ucModalTitle = ucModal.querySelector("h2");
const ucModalInputs = ucModal.querySelectorAll(".ucTextInput");
const ucModalForm = ucModal.querySelector("form");
let editingTask = null;

/* -------------------- Create/Edit Modal -------------------- */
function showCreateModal() {
	ucModalInputs.forEach((input) => (input.value = ""));
	ucModalTitle.innerText = "Create Task";
	editingTask = null;

	tint.style.display = "block";
	ucModal.style.display = "flex";
}

function showEditModal(task) {
	ucModalInputs[0].value = task.name;
	ucModalInputs[1].value = task.description;
	ucModalInputs[2].value = task.endDate.substring(0, 10);
	ucModalInputs[3].value = task.priority;
	ucModalInputs[4].value = task.category;
	ucModalInputs[5].value = task.status.toUpperCase();

	ucModalTitle.innerText = "Edit Task";
	editingTask = task;

	readModal.style.display = "none";
	ucModal.style.display = "flex";
}

function closeUcModal() {
	tint.style.display = "none";
	ucModal.style.display = "none";
}

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

	if (editingTask) {
		TaskService.update(editingTask, data);
	} else {
		TaskService.create(data);
	}

	closeUcModal();
	refreshTaskList();
});

/* -------------------- Read Modal -------------------- */
function showReadModal(task) {
	readModalEditButton.removeEventListener("click", readModalEditListener);
	readModalDeleteButton.removeEventListener("click", readModalDeleteListener);

	readModalStatus.innerText = task.status;
	readModalName.innerText = task.name;
	readModalDescription.innerText = task.description;
	readModalEndDate.innerText = "End Date: " + task.endDate.substring(0, 10);
	readModalCategory.innerText = "Category: " + task.category;
	readModalPriority.innerText = task.priority;
	readModalPriority.style.backgroundColor = getPriorityColor(task.priority);

	readModalEditListener = () => showEditModal(task);
	readModalDeleteListener = () => showDeleteModal(task);

	readModalEditButton.addEventListener("click", readModalEditListener);
	readModalDeleteButton.addEventListener("click", readModalDeleteListener);

	tint.style.display = "block";
	readModal.style.display = "flex";
}

function closeReadModal() {
	tint.style.display = "none";
	readModal.style.display = "none";
}

/* -------------------- Delete Modal -------------------- */
const deleteModal = document.querySelector(".deleteModalContainer");
const deleteModalDeleteButton = deleteModal.querySelector(".delete");
let handleDeleteListener;

function showDeleteModal(task) {
	deleteModalDeleteButton.removeEventListener("click", handleDeleteListener);
	handleDeleteListener = () => {
		TaskService.delete(task);
		closeDeleteModal();
		closeReadModal();
		refreshTaskList();
	};
	deleteModalDeleteButton.addEventListener("click", handleDeleteListener);

	tint.style.zIndex = "1050";
	deleteModal.style.display = "flex";
}

function closeDeleteModal() {
	tint.style.zIndex = "900";
	deleteModal.style.display = "none";
}

/* -------------------- Helpers -------------------- */
function getPriorityColor(priority) {
	return colors[`Priority${parseInt(priority)}`];
}

function refreshTaskList() {
	if (selectedFilter) {
		loadTasksByStatus(selectedFilter.innerText);
	} else {
		loadTasksByPriority();
	}
}

/* -------------------- Rendering -------------------- */
const listContainer = document.querySelector(".listContainer");

function taskCard(task) {
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

function loadTasksByPriority() {
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

function loadTasksByStatus(status) {
	const filteredTasks = TaskService.list.filter(
		(task) => task.status === status
	);
	listContainer.innerHTML = "";
	filteredTasks.forEach((task) => {
		listContainer.appendChild(taskCard(task));
	});
}
