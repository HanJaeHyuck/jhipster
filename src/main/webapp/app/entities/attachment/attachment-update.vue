<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="jh001App.attachment.home.createOrEditLabel" data-cy="AttachmentCreateUpdateHeading">Create or edit a Attachment</h2>
        <div>
          <div class="form-group" v-if="attachment.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="attachment.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="attachment-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="attachment-name"
              data-cy="name"
              :class="{ valid: !$v.attachment.name.$invalid, invalid: $v.attachment.name.$invalid }"
              v-model="$v.attachment.name.$model"
              required
            />
            <div v-if="$v.attachment.name.$anyDirty && $v.attachment.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.attachment.name.required"> This field is required. </small>
              <small class="form-text text-danger" v-if="!$v.attachment.name.minLength">
                This field is required to be at least 3 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="attachment-file">File</label>
            <div>
              <div v-if="attachment.file" class="form-text text-danger clearfix">
                <a class="pull-left" v-on:click="openFile(attachment.fileContentType, attachment.file)">open</a><br />
                <span class="pull-left">{{ attachment.fileContentType }}, {{ byteSize(attachment.file) }}</span>
                <button
                  type="button"
                  v-on:click="
                    attachment.file = null;
                    attachment.fileContentType = null;
                  "
                  class="btn btn-secondary btn-xs pull-right"
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                </button>
              </div>
              <input
                type="file"
                ref="file_file"
                id="file_file"
                data-cy="file"
                v-on:change="setFileData($event, attachment, 'file', false)"
              />
            </div>
            <input
              type="hidden"
              class="form-control"
              name="file"
              id="attachment-file"
              data-cy="file"
              :class="{ valid: !$v.attachment.file.$invalid, invalid: $v.attachment.file.$invalid }"
              v-model="$v.attachment.file.$model"
            />
            <input
              type="hidden"
              class="form-control"
              name="fileContentType"
              id="attachment-fileContentType"
              v-model="attachment.fileContentType"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="attachment-ticket">Ticket</label>
            <select class="form-control" id="attachment-ticket" data-cy="ticket" name="ticket" v-model="attachment.ticket">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="attachment.ticket && ticketOption.id === attachment.ticket.id ? attachment.ticket : ticketOption"
                v-for="ticketOption in tickets"
                :key="ticketOption.id"
              >
                {{ ticketOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.attachment.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./attachment-update.component.ts"></script>
