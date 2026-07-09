import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import type { Room } from "@/services/rooms-api";
import type { Facility } from "@/services/facilities-api";

const roomSchema = z.object({
  name: z.string().min(1, "Room name is required"),
  facilityId: z.string().min(1, "Facility is required"),
  floor: z.string().min(1, "Floor is required"),
  capacity: z.coerce.number().min(1, "Capacity must be at least 1"),
});

type RoomFormValues = {
  name: string;
  facilityId: string;
  floor: string;
  capacity: number;
};

interface RoomFormProps {
  initialData?: Room;
  facilities: Facility[];
  onSubmit: (data: RoomFormValues) => void;
  onCancel: () => void;
}

export const RoomForm = ({ initialData, facilities, onSubmit, onCancel }: RoomFormProps) => {
  const form = useForm<RoomFormValues>({
    resolver: zodResolver(roomSchema) as any,
    defaultValues: initialData || {
      name: "",
      facilityId: "",
      floor: "",
      capacity: 1,
    },
  });

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit as any)} className="space-y-4">
        <FormField
          control={form.control as any}
          name="name"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Room Name</FormLabel>
              <FormControl>
                <Input placeholder="Enter room name" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control as any}
          name="facilityId"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Facility</FormLabel>
              <Select onValueChange={field.onChange} defaultValue={field.value}>
                <FormControl>
                  <SelectTrigger>
                    <SelectValue placeholder="Select a facility" />
                  </SelectTrigger>
                </FormControl>
                <SelectContent>
                  {facilities.map(f => (
                    <SelectItem key={f.id} value={f.id}>{f.name}</SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <FormMessage />
            </FormItem>
          )}
        />
        <div className="grid grid-cols-2 gap-4">
          <FormField
            control={form.control as any}
            name="floor"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Floor</FormLabel>
                <FormControl>
                  <Input placeholder="e.g. 1st Floor" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control as any}
            name="capacity"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Capacity</FormLabel>
                <FormControl>
                  <Input type="number" min={1} {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
        </div>
        <div className="flex justify-end space-x-2 pt-4">
          <Button variant="outline" type="button" onClick={onCancel}>
            Cancel
          </Button>
          <Button type="submit">Save</Button>
        </div>
      </form>
    </Form>
  );
};
