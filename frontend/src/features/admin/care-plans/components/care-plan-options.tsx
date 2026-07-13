import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Input } from "@/components/ui/input";
import { ChevronDown, Search } from "lucide-react";
import { useState } from "react";
import Text from "../ui/Text";

export default function CarePlanOptions() {
  const [status, setStatus] = useState("All");
  const [review, setReview] = useState("All");
  return (
    <div className="flex flex-row items-center flex-wrap gap-3">
      <div className="relative flex-1 min-w-[300px] ">
        <Search className="absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-gray-400" />

        <Input
          className=" border-gray-400 bg-[#fafcfe] w-full  pl-10 text-lg h-15"
          placeholder="Search residents..."
        ></Input>
      </div>

      <DropdownMenu>
        <DropdownMenuTrigger>
          <button className="border-2 border-gray-400 bg-[#fafcfe] rounded-lg flex items-center justify-between px-4 h-15">
            <Text>Status: {status}</Text>
            <ChevronDown className="h-4 w-4" />
          </button>
        </DropdownMenuTrigger>

        <DropdownMenuContent>
          <DropdownMenuItem
            onClick={() => {
              setStatus("All");
            }}
          >
            <div>All</div>
          </DropdownMenuItem>

          <DropdownMenuItem
            onClick={() => {
              setStatus("Item1");
            }}
          >
            <div>Item1</div>
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>

      <DropdownMenu>
        <DropdownMenuTrigger>
          <button className=" border-2 border-gray-400 bg-[#fafcfe] rounded-lg flex items-center justify-between px-4 h-15">
            <Text>
              Review: {review}
            </Text>

              <ChevronDown className="h-4 w-4" />
          </button>
        </DropdownMenuTrigger>

        <DropdownMenuContent>
          <DropdownMenuItem
            onClick={() => {
              setReview("All");
            }}
          >
            <div>All</div>
          </DropdownMenuItem>

          <DropdownMenuItem
            onClick={() => {
              setReview("Item1");
            }}
          >
            <div>Item1</div>
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
      <Button
        className={`border-2 border-solid border-gray-400  bg-[#fafcfe]  flex items-center justify-center rounded-[8px] text-black h-15 `}
      >
        Board
      </Button>

      <Button className={`md:h-[60px] md:w-[200px] `}>+ New Care Plan</Button>
    </div>
  );
}
