import { TaskService } from "../Store.js";
import { closeReadModal } from "./ReadModal.js";
import { refreshTaskList } from "../common/Utils.js";

export function showDeleteModal(task) {
	const deleteModal = document.querySelector(".deleteModalContainer");
	const deleteModalDeleteButton = deleteModal.querySelector(".delete");
	const tint = document.querySelector(".tint");
	let handleDeleteListener;

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

export function closeDeleteModal() {
	const tint = document.querySelector(".tint");
	const deleteModal = document.querySelector(".deleteModalContainer");

	tint.style.zIndex = "900";
	deleteModal.style.display = "none";
}
