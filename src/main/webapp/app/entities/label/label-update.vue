<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="jh001App.label.home.createOrEditLabel" data-cy="LabelCreateUpdateHeading">Create or edit a Label</h2>
        <div>
          <div class="form-group" v-if="label.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="label.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="label-label">Label</label>
            <input
              type="text"
              class="form-control"
              name="label"
              id="label-label"
              data-cy="label"
              :class="{ valid: !$v.label.label.$invalid, invalid: $v.label.label.$invalid }"
              v-model="$v.label.label.$model"
              required
            />
            <div v-if="$v.label.label.$anyDirty && $v.label.label.$invalid">
              <small class="form-text text-danger" v-if="!$v.label.label.required"> This field is required. </small>
              <small class="form-text text-danger" v-if="!$v.label.label.minLength">
                This field is required to be at least 3 characters.
              </small>
            </div>
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
            :disabled="$v.label.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./label-update.component.ts"></script>
