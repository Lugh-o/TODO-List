import { showDeleteModal } from "./DeleteModal.js";
import { showEditModal } from "./UcModal.js";
import { getPriorityColor } from "../com.acelerazg/Utils.js";

export function showReadModal(task) {
	const readModal = document.querySelector(".readModalContainer");
	const readModalEditButton = readModal.querySelector(".edit");
	let readModalEditListener, readModalDeleteListener;
	const tint = document.querySelector(".tint");

	const readModalStatus = readModal.querySelector(".cardStatus");
	const readModalName = readModal.querySelector(".cardName");
	const readModalDescription = readModal.querySelector(".cardDescription");
	const readModalEndDate = readModal.querySelector(".cardEndDate");
	const readModalCategory = readModal.querySelector(".cardCategory");
	const readModalPriority = readModal.querySelector(".cardPriority");
	const readModalDeleteButton = readModal.querySelector(".delete");

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

export function closeReadModal() {
	const readModal = document.querySelector(".readModalContainer");
	const tint = document.querySelector(".tint");

	tint.style.display = "none";
	readModal.style.display = "none";
}
